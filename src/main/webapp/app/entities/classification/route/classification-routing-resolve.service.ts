import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassification } from '../classification.model';
import { ClassificationService } from '../service/classification.service';

export const classificationResolve = (route: ActivatedRouteSnapshot): Observable<null | IClassification> => {
  const id = route.params['id'];
  if (id) {
    return inject(ClassificationService)
      .find(id)
      .pipe(
        mergeMap((classification: HttpResponse<IClassification>) => {
          if (classification.body) {
            return of(classification.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default classificationResolve;
