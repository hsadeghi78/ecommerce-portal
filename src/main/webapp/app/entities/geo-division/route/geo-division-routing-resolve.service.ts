import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGeoDivision } from '../geo-division.model';
import { GeoDivisionService } from '../service/geo-division.service';

export const geoDivisionResolve = (route: ActivatedRouteSnapshot): Observable<null | IGeoDivision> => {
  const id = route.params['id'];
  if (id) {
    return inject(GeoDivisionService)
      .find(id)
      .pipe(
        mergeMap((geoDivision: HttpResponse<IGeoDivision>) => {
          if (geoDivision.body) {
            return of(geoDivision.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default geoDivisionResolve;
