import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MyAuthorityComponent } from './list/my-authority.component';
import { MyAuthorityDetailComponent } from './detail/my-authority-detail.component';
import { MyAuthorityUpdateComponent } from './update/my-authority-update.component';
import MyAuthorityResolve from './route/my-authority-routing-resolve.service';

const myAuthorityRoute: Routes = [
  {
    path: '',
    component: MyAuthorityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MyAuthorityDetailComponent,
    resolve: {
      myAuthority: MyAuthorityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MyAuthorityUpdateComponent,
    resolve: {
      myAuthority: MyAuthorityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MyAuthorityUpdateComponent,
    resolve: {
      myAuthority: MyAuthorityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default myAuthorityRoute;
