import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ClassificationComponent } from './list/classification.component';
import { ClassificationDetailComponent } from './detail/classification-detail.component';
import { ClassificationUpdateComponent } from './update/classification-update.component';
import ClassificationResolve from './route/classification-routing-resolve.service';

const classificationRoute: Routes = [
  {
    path: '',
    component: ClassificationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassificationDetailComponent,
    resolve: {
      classification: ClassificationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassificationUpdateComponent,
    resolve: {
      classification: ClassificationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassificationUpdateComponent,
    resolve: {
      classification: ClassificationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default classificationRoute;
