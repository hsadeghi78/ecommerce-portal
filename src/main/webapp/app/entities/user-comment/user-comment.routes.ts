import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UserCommentComponent } from './list/user-comment.component';
import { UserCommentDetailComponent } from './detail/user-comment-detail.component';
import { UserCommentUpdateComponent } from './update/user-comment-update.component';
import UserCommentResolve from './route/user-comment-routing-resolve.service';

const userCommentRoute: Routes = [
  {
    path: '',
    component: UserCommentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserCommentDetailComponent,
    resolve: {
      userComment: UserCommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserCommentUpdateComponent,
    resolve: {
      userComment: UserCommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserCommentUpdateComponent,
    resolve: {
      userComment: UserCommentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userCommentRoute;
