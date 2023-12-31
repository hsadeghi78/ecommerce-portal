import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ClassTypeComponent } from './list/class-type.component';
import { ClassTypeDetailComponent } from './detail/class-type-detail.component';
import { ClassTypeUpdateComponent } from './update/class-type-update.component';
import ClassTypeResolve from './route/class-type-routing-resolve.service';

const classTypeRoute: Routes = [
  {
    path: '',
    component: ClassTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassTypeDetailComponent,
    resolve: {
      classType: ClassTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassTypeUpdateComponent,
    resolve: {
      classType: ClassTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassTypeUpdateComponent,
    resolve: {
      classType: ClassTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default classTypeRoute;
