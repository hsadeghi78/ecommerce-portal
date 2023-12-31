import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConsumeMaterial } from '../consume-material.model';
import { ConsumeMaterialService } from '../service/consume-material.service';

export const consumeMaterialResolve = (route: ActivatedRouteSnapshot): Observable<null | IConsumeMaterial> => {
  const id = route.params['id'];
  if (id) {
    return inject(ConsumeMaterialService)
      .find(id)
      .pipe(
        mergeMap((consumeMaterial: HttpResponse<IConsumeMaterial>) => {
          if (consumeMaterial.body) {
            return of(consumeMaterial.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default consumeMaterialResolve;
