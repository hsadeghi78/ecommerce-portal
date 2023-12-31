import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFactor, NewFactor } from '../factor.model';

export type PartialUpdateFactor = Partial<IFactor> & Pick<IFactor, 'id'>;

export type EntityResponseType = HttpResponse<IFactor>;
export type EntityArrayResponseType = HttpResponse<IFactor[]>;

@Injectable({ providedIn: 'root' })
export class FactorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/factors');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(factor: NewFactor): Observable<EntityResponseType> {
    return this.http.post<IFactor>(this.resourceUrl, factor, { observe: 'response' });
  }

  update(factor: IFactor): Observable<EntityResponseType> {
    return this.http.put<IFactor>(`${this.resourceUrl}/${this.getFactorIdentifier(factor)}`, factor, { observe: 'response' });
  }

  partialUpdate(factor: PartialUpdateFactor): Observable<EntityResponseType> {
    return this.http.patch<IFactor>(`${this.resourceUrl}/${this.getFactorIdentifier(factor)}`, factor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFactor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFactorIdentifier(factor: Pick<IFactor, 'id'>): number {
    return factor.id;
  }

  compareFactor(o1: Pick<IFactor, 'id'> | null, o2: Pick<IFactor, 'id'> | null): boolean {
    return o1 && o2 ? this.getFactorIdentifier(o1) === this.getFactorIdentifier(o2) : o1 === o2;
  }

  addFactorToCollectionIfMissing<Type extends Pick<IFactor, 'id'>>(
    factorCollection: Type[],
    ...factorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const factors: Type[] = factorsToCheck.filter(isPresent);
    if (factors.length > 0) {
      const factorCollectionIdentifiers = factorCollection.map(factorItem => this.getFactorIdentifier(factorItem)!);
      const factorsToAdd = factors.filter(factorItem => {
        const factorIdentifier = this.getFactorIdentifier(factorItem);
        if (factorCollectionIdentifiers.includes(factorIdentifier)) {
          return false;
        }
        factorCollectionIdentifiers.push(factorIdentifier);
        return true;
      });
      return [...factorsToAdd, ...factorCollection];
    }
    return factorCollection;
  }
}
