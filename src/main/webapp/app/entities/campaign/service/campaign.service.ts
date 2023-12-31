import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICampaign, NewCampaign } from '../campaign.model';

export type PartialUpdateCampaign = Partial<ICampaign> & Pick<ICampaign, 'id'>;

type RestOf<T extends ICampaign | NewCampaign> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestCampaign = RestOf<ICampaign>;

export type NewRestCampaign = RestOf<NewCampaign>;

export type PartialUpdateRestCampaign = RestOf<PartialUpdateCampaign>;

export type EntityResponseType = HttpResponse<ICampaign>;
export type EntityArrayResponseType = HttpResponse<ICampaign[]>;

@Injectable({ providedIn: 'root' })
export class CampaignService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/campaigns');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(campaign: NewCampaign): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(campaign);
    return this.http
      .post<RestCampaign>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(campaign: ICampaign): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(campaign);
    return this.http
      .put<RestCampaign>(`${this.resourceUrl}/${this.getCampaignIdentifier(campaign)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(campaign: PartialUpdateCampaign): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(campaign);
    return this.http
      .patch<RestCampaign>(`${this.resourceUrl}/${this.getCampaignIdentifier(campaign)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCampaign>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCampaign[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCampaignIdentifier(campaign: Pick<ICampaign, 'id'>): number {
    return campaign.id;
  }

  compareCampaign(o1: Pick<ICampaign, 'id'> | null, o2: Pick<ICampaign, 'id'> | null): boolean {
    return o1 && o2 ? this.getCampaignIdentifier(o1) === this.getCampaignIdentifier(o2) : o1 === o2;
  }

  addCampaignToCollectionIfMissing<Type extends Pick<ICampaign, 'id'>>(
    campaignCollection: Type[],
    ...campaignsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const campaigns: Type[] = campaignsToCheck.filter(isPresent);
    if (campaigns.length > 0) {
      const campaignCollectionIdentifiers = campaignCollection.map(campaignItem => this.getCampaignIdentifier(campaignItem)!);
      const campaignsToAdd = campaigns.filter(campaignItem => {
        const campaignIdentifier = this.getCampaignIdentifier(campaignItem);
        if (campaignCollectionIdentifiers.includes(campaignIdentifier)) {
          return false;
        }
        campaignCollectionIdentifiers.push(campaignIdentifier);
        return true;
      });
      return [...campaignsToAdd, ...campaignCollection];
    }
    return campaignCollection;
  }

  protected convertDateFromClient<T extends ICampaign | NewCampaign | PartialUpdateCampaign>(campaign: T): RestOf<T> {
    return {
      ...campaign,
      startDate: campaign.startDate?.format(DATE_FORMAT) ?? null,
      endDate: campaign.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCampaign: RestCampaign): ICampaign {
    return {
      ...restCampaign,
      startDate: restCampaign.startDate ? dayjs(restCampaign.startDate) : undefined,
      endDate: restCampaign.endDate ? dayjs(restCampaign.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCampaign>): HttpResponse<ICampaign> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCampaign[]>): HttpResponse<ICampaign[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
