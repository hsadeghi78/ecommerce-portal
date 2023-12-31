import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAgreement, NewAgreement } from '../agreement.model';

export type PartialUpdateAgreement = Partial<IAgreement> & Pick<IAgreement, 'id'>;

type RestOf<T extends IAgreement | NewAgreement> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestAgreement = RestOf<IAgreement>;

export type NewRestAgreement = RestOf<NewAgreement>;

export type PartialUpdateRestAgreement = RestOf<PartialUpdateAgreement>;

export type EntityResponseType = HttpResponse<IAgreement>;
export type EntityArrayResponseType = HttpResponse<IAgreement[]>;

@Injectable({ providedIn: 'root' })
export class AgreementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agreements');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(agreement: NewAgreement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agreement);
    return this.http
      .post<RestAgreement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(agreement: IAgreement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agreement);
    return this.http
      .put<RestAgreement>(`${this.resourceUrl}/${this.getAgreementIdentifier(agreement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(agreement: PartialUpdateAgreement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agreement);
    return this.http
      .patch<RestAgreement>(`${this.resourceUrl}/${this.getAgreementIdentifier(agreement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAgreement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAgreement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAgreementIdentifier(agreement: Pick<IAgreement, 'id'>): number {
    return agreement.id;
  }

  compareAgreement(o1: Pick<IAgreement, 'id'> | null, o2: Pick<IAgreement, 'id'> | null): boolean {
    return o1 && o2 ? this.getAgreementIdentifier(o1) === this.getAgreementIdentifier(o2) : o1 === o2;
  }

  addAgreementToCollectionIfMissing<Type extends Pick<IAgreement, 'id'>>(
    agreementCollection: Type[],
    ...agreementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const agreements: Type[] = agreementsToCheck.filter(isPresent);
    if (agreements.length > 0) {
      const agreementCollectionIdentifiers = agreementCollection.map(agreementItem => this.getAgreementIdentifier(agreementItem)!);
      const agreementsToAdd = agreements.filter(agreementItem => {
        const agreementIdentifier = this.getAgreementIdentifier(agreementItem);
        if (agreementCollectionIdentifiers.includes(agreementIdentifier)) {
          return false;
        }
        agreementCollectionIdentifiers.push(agreementIdentifier);
        return true;
      });
      return [...agreementsToAdd, ...agreementCollection];
    }
    return agreementCollection;
  }

  protected convertDateFromClient<T extends IAgreement | NewAgreement | PartialUpdateAgreement>(agreement: T): RestOf<T> {
    return {
      ...agreement,
      startDate: agreement.startDate?.format(DATE_FORMAT) ?? null,
      endDate: agreement.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAgreement: RestAgreement): IAgreement {
    return {
      ...restAgreement,
      startDate: restAgreement.startDate ? dayjs(restAgreement.startDate) : undefined,
      endDate: restAgreement.endDate ? dayjs(restAgreement.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAgreement>): HttpResponse<IAgreement> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAgreement[]>): HttpResponse<IAgreement[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
