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
import { IRuolo, NewRuolo } from '../ruolo.model';

export type PartialUpdateRuolo = Partial<IRuolo> & Pick<IRuolo, 'id'>;

type RestOf<T extends IRuolo | NewRuolo> = Omit<T, 'created' | 'modified'> & {
  created?: string | null;
  modified?: string | null;
};

export type RestRuolo = RestOf<IRuolo>;

export type NewRestRuolo = RestOf<NewRuolo>;

export type PartialUpdateRestRuolo = RestOf<PartialUpdateRuolo>;

export type EntityResponseType = HttpResponse<IRuolo>;
export type EntityArrayResponseType = HttpResponse<IRuolo[]>;

@Injectable({ providedIn: 'root' })
export class RuoloService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ruolos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/ruolos/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(ruolo: NewRuolo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ruolo);
    return this.http.post<RestRuolo>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ruolo: IRuolo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ruolo);
    return this.http
      .put<RestRuolo>(`${this.resourceUrl}/${this.getRuoloIdentifier(ruolo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ruolo: PartialUpdateRuolo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ruolo);
    return this.http
      .patch<RestRuolo>(`${this.resourceUrl}/${this.getRuoloIdentifier(ruolo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRuolo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRuolo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestRuolo[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IRuolo[]>()], asapScheduler)),
    );
  }

  getRuoloIdentifier(ruolo: Pick<IRuolo, 'id'>): number {
    return ruolo.id;
  }

  compareRuolo(o1: Pick<IRuolo, 'id'> | null, o2: Pick<IRuolo, 'id'> | null): boolean {
    return o1 && o2 ? this.getRuoloIdentifier(o1) === this.getRuoloIdentifier(o2) : o1 === o2;
  }

  addRuoloToCollectionIfMissing<Type extends Pick<IRuolo, 'id'>>(
    ruoloCollection: Type[],
    ...ruolosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ruolos: Type[] = ruolosToCheck.filter(isPresent);
    if (ruolos.length > 0) {
      const ruoloCollectionIdentifiers = ruoloCollection.map(ruoloItem => this.getRuoloIdentifier(ruoloItem)!);
      const ruolosToAdd = ruolos.filter(ruoloItem => {
        const ruoloIdentifier = this.getRuoloIdentifier(ruoloItem);
        if (ruoloCollectionIdentifiers.includes(ruoloIdentifier)) {
          return false;
        }
        ruoloCollectionIdentifiers.push(ruoloIdentifier);
        return true;
      });
      return [...ruolosToAdd, ...ruoloCollection];
    }
    return ruoloCollection;
  }

  protected convertDateFromClient<T extends IRuolo | NewRuolo | PartialUpdateRuolo>(ruolo: T): RestOf<T> {
    return {
      ...ruolo,
      created: ruolo.created?.format(DATE_FORMAT) ?? null,
      modified: ruolo.modified?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restRuolo: RestRuolo): IRuolo {
    return {
      ...restRuolo,
      created: restRuolo.created ? dayjs(restRuolo.created) : undefined,
      modified: restRuolo.modified ? dayjs(restRuolo.modified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRuolo>): HttpResponse<IRuolo> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRuolo[]>): HttpResponse<IRuolo[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
