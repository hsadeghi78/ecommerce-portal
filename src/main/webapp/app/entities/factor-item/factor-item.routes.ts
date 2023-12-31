import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FactorItemComponent } from './list/factor-item.component';
import { FactorItemDetailComponent } from './detail/factor-item-detail.component';
import { FactorItemUpdateComponent } from './update/factor-item-update.component';
import FactorItemResolve from './route/factor-item-routing-resolve.service';

const factorItemRoute: Routes = [
  {
    path: '',
    component: FactorItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FactorItemDetailComponent,
    resolve: {
      factorItem: FactorItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FactorItemUpdateComponent,
    resolve: {
      factorItem: FactorItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FactorItemUpdateComponent,
    resolve: {
      factorItem: FactorItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default factorItemRoute;
