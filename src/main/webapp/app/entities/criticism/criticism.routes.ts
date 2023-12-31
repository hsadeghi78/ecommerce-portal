import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CriticismComponent } from './list/criticism.component';
import { CriticismDetailComponent } from './detail/criticism-detail.component';
import { CriticismUpdateComponent } from './update/criticism-update.component';
import CriticismResolve from './route/criticism-routing-resolve.service';

const criticismRoute: Routes = [
  {
    path: '',
    component: CriticismComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CriticismDetailComponent,
    resolve: {
      criticism: CriticismResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CriticismUpdateComponent,
    resolve: {
      criticism: CriticismResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CriticismUpdateComponent,
    resolve: {
      criticism: CriticismResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default criticismRoute;
