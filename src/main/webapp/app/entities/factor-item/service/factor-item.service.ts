import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFactorItem, NewFactorItem } from '../factor-item.model';

export type PartialUpdateFactorItem = Partial<IFactorItem> & Pick<IFactorItem, 'id'>;

export type EntityResponseType = HttpResponse<IFactorItem>;
export type EntityArrayResponseType = HttpResponse<IFactorItem[]>;

@Injectable({ providedIn: 'root' })
export class FactorItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/factor-items');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(factorItem: NewFactorItem): Observable<EntityResponseType> {
    return this.http.post<IFactorItem>(this.resourceUrl, factorItem, { observe: 'response' });
  }

  update(factorItem: IFactorItem): Observable<EntityResponseType> {
    return this.http.put<IFactorItem>(`${this.resourceUrl}/${this.getFactorItemIdentifier(factorItem)}`, factorItem, {
      observe: 'response',
    });
  }

  partialUpdate(factorItem: PartialUpdateFactorItem): Observable<EntityResponseType> {
    return this.http.patch<IFactorItem>(`${this.resourceUrl}/${this.getFactorItemIdentifier(factorItem)}`, factorItem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFactorItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactorItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFactorItemIdentifier(factorItem: Pick<IFactorItem, 'id'>): number {
    return factorItem.id;
  }

  compareFactorItem(o1: Pick<IFactorItem, 'id'> | null, o2: Pick<IFactorItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getFactorItemIdentifier(o1) === this.getFactorItemIdentifier(o2) : o1 === o2;
  }

  addFactorItemToCollectionIfMissing<Type extends Pick<IFactorItem, 'id'>>(
    factorItemCollection: Type[],
    ...factorItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const factorItems: Type[] = factorItemsToCheck.filter(isPresent);
    if (factorItems.length > 0) {
      const factorItemCollectionIdentifiers = factorItemCollection.map(factorItemItem => this.getFactorItemIdentifier(factorItemItem)!);
      const factorItemsToAdd = factorItems.filter(factorItemItem => {
        const factorItemIdentifier = this.getFactorItemIdentifier(factorItemItem);
        if (factorItemCollectionIdentifiers.includes(factorItemIdentifier)) {
          return false;
        }
        factorItemCollectionIdentifiers.push(factorItemIdentifier);
        return true;
      });
      return [...factorItemsToAdd, ...factorItemCollection];
    }
    return factorItemCollection;
  }
}
