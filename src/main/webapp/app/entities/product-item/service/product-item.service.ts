import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductItem, NewProductItem } from '../product-item.model';

export type PartialUpdateProductItem = Partial<IProductItem> & Pick<IProductItem, 'id'>;

export type EntityResponseType = HttpResponse<IProductItem>;
export type EntityArrayResponseType = HttpResponse<IProductItem[]>;

@Injectable({ providedIn: 'root' })
export class ProductItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-items');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(productItem: NewProductItem): Observable<EntityResponseType> {
    return this.http.post<IProductItem>(this.resourceUrl, productItem, { observe: 'response' });
  }

  update(productItem: IProductItem): Observable<EntityResponseType> {
    return this.http.put<IProductItem>(`${this.resourceUrl}/${this.getProductItemIdentifier(productItem)}`, productItem, {
      observe: 'response',
    });
  }

  partialUpdate(productItem: PartialUpdateProductItem): Observable<EntityResponseType> {
    return this.http.patch<IProductItem>(`${this.resourceUrl}/${this.getProductItemIdentifier(productItem)}`, productItem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductItemIdentifier(productItem: Pick<IProductItem, 'id'>): number {
    return productItem.id;
  }

  compareProductItem(o1: Pick<IProductItem, 'id'> | null, o2: Pick<IProductItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductItemIdentifier(o1) === this.getProductItemIdentifier(o2) : o1 === o2;
  }

  addProductItemToCollectionIfMissing<Type extends Pick<IProductItem, 'id'>>(
    productItemCollection: Type[],
    ...productItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productItems: Type[] = productItemsToCheck.filter(isPresent);
    if (productItems.length > 0) {
      const productItemCollectionIdentifiers = productItemCollection.map(
        productItemItem => this.getProductItemIdentifier(productItemItem)!,
      );
      const productItemsToAdd = productItems.filter(productItemItem => {
        const productItemIdentifier = this.getProductItemIdentifier(productItemItem);
        if (productItemCollectionIdentifiers.includes(productItemIdentifier)) {
          return false;
        }
        productItemCollectionIdentifiers.push(productItemIdentifier);
        return true;
      });
      return [...productItemsToAdd, ...productItemCollection];
    }
    return productItemCollection;
  }
}
