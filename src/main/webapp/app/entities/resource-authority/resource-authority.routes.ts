import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ResourceAuthorityComponent } from './list/resource-authority.component';
import { ResourceAuthorityDetailComponent } from './detail/resource-authority-detail.component';
import { ResourceAuthorityUpdateComponent } from './update/resource-authority-update.component';
import ResourceAuthorityResolve from './route/resource-authority-routing-resolve.service';

const resourceAuthorityRoute: Routes = [
  {
    path: '',
    component: ResourceAuthorityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResourceAuthorityDetailComponent,
    resolve: {
      resourceAuthority: ResourceAuthorityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResourceAuthorityUpdateComponent,
    resolve: {
      resourceAuthority: ResourceAuthorityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResourceAuthorityUpdateComponent,
    resolve: {
      resourceAuthority: ResourceAuthorityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default resourceAuthorityRoute;
