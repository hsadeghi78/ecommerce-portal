import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICampaign } from '../campaign.model';
import { CampaignService } from '../service/campaign.service';

export const campaignResolve = (route: ActivatedRouteSnapshot): Observable<null | ICampaign> => {
  const id = route.params['id'];
  if (id) {
    return inject(CampaignService)
      .find(id)
      .pipe(
        mergeMap((campaign: HttpResponse<ICampaign>) => {
          if (campaign.body) {
            return of(campaign.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default campaignResolve;
