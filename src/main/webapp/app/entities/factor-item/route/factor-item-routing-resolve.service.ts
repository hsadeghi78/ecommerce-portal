import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactorItem } from '../factor-item.model';
import { FactorItemService } from '../service/factor-item.service';

export const factorItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IFactorItem> => {
  const id = route.params['id'];
  if (id) {
    return inject(FactorItemService)
      .find(id)
      .pipe(
        mergeMap((factorItem: HttpResponse<IFactorItem>) => {
          if (factorItem.body) {
            return of(factorItem.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default factorItemResolve;
