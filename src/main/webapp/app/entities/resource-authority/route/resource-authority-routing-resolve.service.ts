import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResourceAuthority } from '../resource-authority.model';
import { ResourceAuthorityService } from '../service/resource-authority.service';

export const resourceAuthorityResolve = (route: ActivatedRouteSnapshot): Observable<null | IResourceAuthority> => {
  const id = route.params['id'];
  if (id) {
    return inject(ResourceAuthorityService)
      .find(id)
      .pipe(
        mergeMap((resourceAuthority: HttpResponse<IResourceAuthority>) => {
          if (resourceAuthority.body) {
            return of(resourceAuthority.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default resourceAuthorityResolve;
