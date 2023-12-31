import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICriticism } from '../criticism.model';
import { CriticismService } from '../service/criticism.service';

export const criticismResolve = (route: ActivatedRouteSnapshot): Observable<null | ICriticism> => {
  const id = route.params['id'];
  if (id) {
    return inject(CriticismService)
      .find(id)
      .pipe(
        mergeMap((criticism: HttpResponse<ICriticism>) => {
          if (criticism.body) {
            return of(criticism.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default criticismResolve;
