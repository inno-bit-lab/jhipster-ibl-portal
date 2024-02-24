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
import { IUtente, NewUtente } from '../utente.model';

export type PartialUpdateUtente = Partial<IUtente> & Pick<IUtente, 'id'>;

type RestOf<T extends IUtente | NewUtente> = Omit<T, 'created' | 'modified' | 'dataBolocco' | 'registrationDate' | 'lastAccess'> & {
  created?: string | null;
  modified?: string | null;
  dataBolocco?: string | null;
  registrationDate?: string | null;
  lastAccess?: string | null;
};

export type RestUtente = RestOf<IUtente>;

export type NewRestUtente = RestOf<NewUtente>;

export type PartialUpdateRestUtente = RestOf<PartialUpdateUtente>;

export type EntityResponseType = HttpResponse<IUtente>;
export type EntityArrayResponseType = HttpResponse<IUtente[]>;

@Injectable({ providedIn: 'root' })
export class UtenteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/utentes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/utentes/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(utente: NewUtente): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(utente);
    return this.http
      .post<RestUtente>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(utente: IUtente): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(utente);
    return this.http
      .put<RestUtente>(`${this.resourceUrl}/${this.getUtenteIdentifier(utente)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(utente: PartialUpdateUtente): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(utente);
    return this.http
      .patch<RestUtente>(`${this.resourceUrl}/${this.getUtenteIdentifier(utente)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUtente>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUtente[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestUtente[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IUtente[]>()], asapScheduler)),
    );
  }

  getUtenteIdentifier(utente: Pick<IUtente, 'id'>): number {
    return utente.id;
  }

  compareUtente(o1: Pick<IUtente, 'id'> | null, o2: Pick<IUtente, 'id'> | null): boolean {
    return o1 && o2 ? this.getUtenteIdentifier(o1) === this.getUtenteIdentifier(o2) : o1 === o2;
  }

  addUtenteToCollectionIfMissing<Type extends Pick<IUtente, 'id'>>(
    utenteCollection: Type[],
    ...utentesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const utentes: Type[] = utentesToCheck.filter(isPresent);
    if (utentes.length > 0) {
      const utenteCollectionIdentifiers = utenteCollection.map(utenteItem => this.getUtenteIdentifier(utenteItem)!);
      const utentesToAdd = utentes.filter(utenteItem => {
        const utenteIdentifier = this.getUtenteIdentifier(utenteItem);
        if (utenteCollectionIdentifiers.includes(utenteIdentifier)) {
          return false;
        }
        utenteCollectionIdentifiers.push(utenteIdentifier);
        return true;
      });
      return [...utentesToAdd, ...utenteCollection];
    }
    return utenteCollection;
  }

  protected convertDateFromClient<T extends IUtente | NewUtente | PartialUpdateUtente>(utente: T): RestOf<T> {
    return {
      ...utente,
      created: utente.created?.format(DATE_FORMAT) ?? null,
      modified: utente.modified?.format(DATE_FORMAT) ?? null,
      dataBolocco: utente.dataBolocco?.format(DATE_FORMAT) ?? null,
      registrationDate: utente.registrationDate?.format(DATE_FORMAT) ?? null,
      lastAccess: utente.lastAccess?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restUtente: RestUtente): IUtente {
    return {
      ...restUtente,
      created: restUtente.created ? dayjs(restUtente.created) : undefined,
      modified: restUtente.modified ? dayjs(restUtente.modified) : undefined,
      dataBolocco: restUtente.dataBolocco ? dayjs(restUtente.dataBolocco) : undefined,
      registrationDate: restUtente.registrationDate ? dayjs(restUtente.registrationDate) : undefined,
      lastAccess: restUtente.lastAccess ? dayjs(restUtente.lastAccess) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUtente>): HttpResponse<IUtente> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUtente[]>): HttpResponse<IUtente[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
