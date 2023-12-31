import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AgreementComponent } from './list/agreement.component';
import { AgreementDetailComponent } from './detail/agreement-detail.component';
import { AgreementUpdateComponent } from './update/agreement-update.component';
import AgreementResolve from './route/agreement-routing-resolve.service';

const agreementRoute: Routes = [
  {
    path: '',
    component: AgreementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AgreementDetailComponent,
    resolve: {
      agreement: AgreementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AgreementUpdateComponent,
    resolve: {
      agreement: AgreementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AgreementUpdateComponent,
    resolve: {
      agreement: AgreementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default agreementRoute;
