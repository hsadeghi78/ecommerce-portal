import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductItem } from '../product-item.model';
import { ProductItemService } from '../service/product-item.service';

export const productItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductItem> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProductItemService)
      .find(id)
      .pipe(
        mergeMap((productItem: HttpResponse<IProductItem>) => {
          if (productItem.body) {
            return of(productItem.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default productItemResolve;
