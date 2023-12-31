import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ConsumeMaterialComponent } from './list/consume-material.component';
import { ConsumeMaterialDetailComponent } from './detail/consume-material-detail.component';
import { ConsumeMaterialUpdateComponent } from './update/consume-material-update.component';
import ConsumeMaterialResolve from './route/consume-material-routing-resolve.service';

const consumeMaterialRoute: Routes = [
  {
    path: '',
    component: ConsumeMaterialComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConsumeMaterialDetailComponent,
    resolve: {
      consumeMaterial: ConsumeMaterialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConsumeMaterialUpdateComponent,
    resolve: {
      consumeMaterial: ConsumeMaterialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConsumeMaterialUpdateComponent,
    resolve: {
      consumeMaterial: ConsumeMaterialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default consumeMaterialRoute;
