import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CampaignComponent } from './list/campaign.component';
import { CampaignDetailComponent } from './detail/campaign-detail.component';
import { CampaignUpdateComponent } from './update/campaign-update.component';
import CampaignResolve from './route/campaign-routing-resolve.service';

const campaignRoute: Routes = [
  {
    path: '',
    component: CampaignComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CampaignDetailComponent,
    resolve: {
      campaign: CampaignResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CampaignUpdateComponent,
    resolve: {
      campaign: CampaignResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CampaignUpdateComponent,
    resolve: {
      campaign: CampaignResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default campaignRoute;
