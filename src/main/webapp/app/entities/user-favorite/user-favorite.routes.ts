import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UserFavoriteComponent } from './list/user-favorite.component';
import { UserFavoriteDetailComponent } from './detail/user-favorite-detail.component';
import { UserFavoriteUpdateComponent } from './update/user-favorite-update.component';
import UserFavoriteResolve from './route/user-favorite-routing-resolve.service';

const userFavoriteRoute: Routes = [
  {
    path: '',
    component: UserFavoriteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserFavoriteDetailComponent,
    resolve: {
      userFavorite: UserFavoriteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserFavoriteUpdateComponent,
    resolve: {
      userFavorite: UserFavoriteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserFavoriteUpdateComponent,
    resolve: {
      userFavorite: UserFavoriteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userFavoriteRoute;
