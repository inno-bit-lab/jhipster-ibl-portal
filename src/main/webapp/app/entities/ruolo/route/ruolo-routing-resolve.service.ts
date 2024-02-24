import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRuolo } from '../ruolo.model';
import { RuoloService } from '../service/ruolo.service';

export const ruoloResolve = (route: ActivatedRouteSnapshot): Observable<null | IRuolo> => {
  const id = route.params['id'];
  if (id) {
    return inject(RuoloService)
      .find(id)
      .pipe(
        mergeMap((ruolo: HttpResponse<IRuolo>) => {
          if (ruolo.body) {
            return of(ruolo.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default ruoloResolve;
