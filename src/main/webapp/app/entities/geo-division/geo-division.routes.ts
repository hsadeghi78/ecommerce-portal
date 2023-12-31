import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { GeoDivisionComponent } from './list/geo-division.component';
import { GeoDivisionDetailComponent } from './detail/geo-division-detail.component';
import { GeoDivisionUpdateComponent } from './update/geo-division-update.component';
import GeoDivisionResolve from './route/geo-division-routing-resolve.service';

const geoDivisionRoute: Routes = [
  {
    path: '',
    component: GeoDivisionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GeoDivisionDetailComponent,
    resolve: {
      geoDivision: GeoDivisionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GeoDivisionUpdateComponent,
    resolve: {
      geoDivision: GeoDivisionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GeoDivisionUpdateComponent,
    resolve: {
      geoDivision: GeoDivisionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default geoDivisionRoute;
