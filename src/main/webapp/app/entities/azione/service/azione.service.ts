import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IAzione, NewAzione } from '../azione.model';

export type PartialUpdateAzione = Partial<IAzione> & Pick<IAzione, 'id'>;

type RestOf<T extends IAzione | NewAzione> = Omit<T, 'created' | 'modified'> & {
  created?: string | null;
  modified?: string | null;
};

export type RestAzione = RestOf<IAzione>;

export type NewRestAzione = RestOf<NewAzione>;

export type PartialUpdateRestAzione = RestOf<PartialUpdateAzione>;

export type EntityResponseType = HttpResponse<IAzione>;
export type EntityArrayResponseType = HttpResponse<IAzione[]>;

@Injectable({ providedIn: 'root' })
export class AzioneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aziones');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/aziones/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(azione: NewAzione): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(azione);
    return this.http
      .post<RestAzione>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(azione: IAzione): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(azione);
    return this.http
      .put<RestAzione>(`${this.resourceUrl}/${this.getAzioneIdentifier(azione)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(azione: PartialUpdateAzione): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(azione);
    return this.http
      .patch<RestAzione>(`${this.resourceUrl}/${this.getAzioneIdentifier(azione)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAzione>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAzione[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestAzione[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IAzione[]>()], asapScheduler)),
    );
  }

  getAzioneIdentifier(azione: Pick<IAzione, 'id'>): number {
    return azione.id;
  }

  compareAzione(o1: Pick<IAzione, 'id'> | null, o2: Pick<IAzione, 'id'> | null): boolean {
    return o1 && o2 ? this.getAzioneIdentifier(o1) === this.getAzioneIdentifier(o2) : o1 === o2;
  }

  addAzioneToCollectionIfMissing<Type extends Pick<IAzione, 'id'>>(
    azioneCollection: Type[],
    ...azionesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aziones: Type[] = azionesToCheck.filter(isPresent);
    if (aziones.length > 0) {
      const azioneCollectionIdentifiers = azioneCollection.map(azioneItem => this.getAzioneIdentifier(azioneItem)!);
      const azionesToAdd = aziones.filter(azioneItem => {
        const azioneIdentifier = this.getAzioneIdentifier(azioneItem);
        if (azioneCollectionIdentifiers.includes(azioneIdentifier)) {
          return false;
        }
        azioneCollectionIdentifiers.push(azioneIdentifier);
        return true;
      });
      return [...azionesToAdd, ...azioneCollection];
    }
    return azioneCollection;
  }

  protected convertDateFromClient<T extends IAzione | NewAzione | PartialUpdateAzione>(azione: T): RestOf<T> {
    return {
      ...azione,
      created: azione.created?.format(DATE_FORMAT) ?? null,
      modified: azione.modified?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAzione: RestAzione): IAzione {
    return {
      ...restAzione,
      created: restAzione.created ? dayjs(restAzione.created) : undefined,
      modified: restAzione.modified ? dayjs(restAzione.modified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAzione>): HttpResponse<IAzione> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAzione[]>): HttpResponse<IAzione[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
