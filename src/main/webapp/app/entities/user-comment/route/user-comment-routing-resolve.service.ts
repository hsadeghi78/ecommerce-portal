import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserComment } from '../user-comment.model';
import { UserCommentService } from '../service/user-comment.service';

export const userCommentResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserComment> => {
  const id = route.params['id'];
  if (id) {
    return inject(UserCommentService)
      .find(id)
      .pipe(
        mergeMap((userComment: HttpResponse<IUserComment>) => {
          if (userComment.body) {
            return of(userComment.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default userCommentResolve;
