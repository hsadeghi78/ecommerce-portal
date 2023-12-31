import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAgreement } from '../agreement.model';
import { AgreementService } from '../service/agreement.service';

export const agreementResolve = (route: ActivatedRouteSnapshot): Observable<null | IAgreement> => {
  const id = route.params['id'];
  if (id) {
    return inject(AgreementService)
      .find(id)
      .pipe(
        mergeMap((agreement: HttpResponse<IAgreement>) => {
          if (agreement.body) {
            return of(agreement.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default agreementResolve;
