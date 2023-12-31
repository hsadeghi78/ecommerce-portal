import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactor } from '../factor.model';
import { FactorService } from '../service/factor.service';

export const factorResolve = (route: ActivatedRouteSnapshot): Observable<null | IFactor> => {
  const id = route.params['id'];
  if (id) {
    return inject(FactorService)
      .find(id)
      .pipe(
        mergeMap((factor: HttpResponse<IFactor>) => {
          if (factor.body) {
            return of(factor.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default factorResolve;
