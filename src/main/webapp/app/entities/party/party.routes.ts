import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PartyComponent } from './list/party.component';
import { PartyDetailComponent } from './detail/party-detail.component';
import { PartyUpdateComponent } from './update/party-update.component';
import PartyResolve from './route/party-routing-resolve.service';

const partyRoute: Routes = [
  {
    path: '',
    component: PartyComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PartyDetailComponent,
    resolve: {
      party: PartyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PartyUpdateComponent,
    resolve: {
      party: PartyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PartyUpdateComponent,
    resolve: {
      party: PartyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default partyRoute;
