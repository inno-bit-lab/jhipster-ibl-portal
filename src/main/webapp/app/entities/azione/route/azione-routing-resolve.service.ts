import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAzione } from '../azione.model';
import { AzioneService } from '../service/azione.service';

export const azioneResolve = (route: ActivatedRouteSnapshot): Observable<null | IAzione> => {
  const id = route.params['id'];
  if (id) {
    return inject(AzioneService)
      .find(id)
      .pipe(
        mergeMap((azione: HttpResponse<IAzione>) => {
          if (azione.body) {
            return of(azione.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default azioneResolve;
