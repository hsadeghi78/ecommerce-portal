import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFileDocument } from '../file-document.model';
import { FileDocumentService } from '../service/file-document.service';

export const fileDocumentResolve = (route: ActivatedRouteSnapshot): Observable<null | IFileDocument> => {
  const id = route.params['id'];
  if (id) {
    return inject(FileDocumentService)
      .find(id)
      .pipe(
        mergeMap((fileDocument: HttpResponse<IFileDocument>) => {
          if (fileDocument.body) {
            return of(fileDocument.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default fileDocumentResolve;
