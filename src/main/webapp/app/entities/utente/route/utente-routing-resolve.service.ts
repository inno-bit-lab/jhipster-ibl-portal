import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUtente } from '../utente.model';
import { UtenteService } from '../service/utente.service';

export const utenteResolve = (route: ActivatedRouteSnapshot): Observable<null | IUtente> => {
  const id = route.params['id'];
  if (id) {
    return inject(UtenteService)
      .find(id)
      .pipe(
        mergeMap((utente: HttpResponse<IUtente>) => {
          if (utente.body) {
            return of(utente.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default utenteResolve;
