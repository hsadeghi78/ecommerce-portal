import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FactorComponent } from './list/factor.component';
import { FactorDetailComponent } from './detail/factor-detail.component';
import { FactorUpdateComponent } from './update/factor-update.component';
import FactorResolve from './route/factor-routing-resolve.service';

const factorRoute: Routes = [
  {
    path: '',
    component: FactorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FactorDetailComponent,
    resolve: {
      factor: FactorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FactorUpdateComponent,
    resolve: {
      factor: FactorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FactorUpdateComponent,
    resolve: {
      factor: FactorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default factorRoute;
