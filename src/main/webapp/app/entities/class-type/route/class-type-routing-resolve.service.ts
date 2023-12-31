import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassType } from '../class-type.model';
import { ClassTypeService } from '../service/class-type.service';

export const classTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IClassType> => {
  const id = route.params['id'];
  if (id) {
    return inject(ClassTypeService)
      .find(id)
      .pipe(
        mergeMap((classType: HttpResponse<IClassType>) => {
          if (classType.body) {
            return of(classType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default classTypeResolve;
