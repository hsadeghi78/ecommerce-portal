import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMyAuthority } from '../my-authority.model';
import { MyAuthorityService } from '../service/my-authority.service';

export const myAuthorityResolve = (route: ActivatedRouteSnapshot): Observable<null | IMyAuthority> => {
  const id = route.params['id'];
  if (id) {
    return inject(MyAuthorityService)
      .find(id)
      .pipe(
        mergeMap((myAuthority: HttpResponse<IMyAuthority>) => {
          if (myAuthority.body) {
            return of(myAuthority.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default myAuthorityResolve;
