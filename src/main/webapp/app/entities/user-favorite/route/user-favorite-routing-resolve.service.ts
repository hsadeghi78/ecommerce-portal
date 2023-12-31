import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserFavorite } from '../user-favorite.model';
import { UserFavoriteService } from '../service/user-favorite.service';

export const userFavoriteResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserFavorite> => {
  const id = route.params['id'];
  if (id) {
    return inject(UserFavoriteService)
      .find(id)
      .pipe(
        mergeMap((userFavorite: HttpResponse<IUserFavorite>) => {
          if (userFavorite.body) {
            return of(userFavorite.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default userFavoriteResolve;
