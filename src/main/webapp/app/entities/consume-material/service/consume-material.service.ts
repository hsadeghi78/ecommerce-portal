import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsumeMaterial, NewConsumeMaterial } from '../consume-material.model';

export type PartialUpdateConsumeMaterial = Partial<IConsumeMaterial> & Pick<IConsumeMaterial, 'id'>;

export type EntityResponseType = HttpResponse<IConsumeMaterial>;
export type EntityArrayResponseType = HttpResponse<IConsumeMaterial[]>;

@Injectable({ providedIn: 'root' })
export class ConsumeMaterialService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/consume-materials');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(consumeMaterial: NewConsumeMaterial): Observable<EntityResponseType> {
    return this.http.post<IConsumeMaterial>(this.resourceUrl, consumeMaterial, { observe: 'response' });
  }

  update(consumeMaterial: IConsumeMaterial): Observable<EntityResponseType> {
    return this.http.put<IConsumeMaterial>(`${this.resourceUrl}/${this.getConsumeMaterialIdentifier(consumeMaterial)}`, consumeMaterial, {
      observe: 'response',
    });
  }

  partialUpdate(consumeMaterial: PartialUpdateConsumeMaterial): Observable<EntityResponseType> {
    return this.http.patch<IConsumeMaterial>(`${this.resourceUrl}/${this.getConsumeMaterialIdentifier(consumeMaterial)}`, consumeMaterial, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConsumeMaterial>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConsumeMaterial[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConsumeMaterialIdentifier(consumeMaterial: Pick<IConsumeMaterial, 'id'>): number {
    return consumeMaterial.id;
  }

  compareConsumeMaterial(o1: Pick<IConsumeMaterial, 'id'> | null, o2: Pick<IConsumeMaterial, 'id'> | null): boolean {
    return o1 && o2 ? this.getConsumeMaterialIdentifier(o1) === this.getConsumeMaterialIdentifier(o2) : o1 === o2;
  }

  addConsumeMaterialToCollectionIfMissing<Type extends Pick<IConsumeMaterial, 'id'>>(
    consumeMaterialCollection: Type[],
    ...consumeMaterialsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const consumeMaterials: Type[] = consumeMaterialsToCheck.filter(isPresent);
    if (consumeMaterials.length > 0) {
      const consumeMaterialCollectionIdentifiers = consumeMaterialCollection.map(
        consumeMaterialItem => this.getConsumeMaterialIdentifier(consumeMaterialItem)!,
      );
      const consumeMaterialsToAdd = consumeMaterials.filter(consumeMaterialItem => {
        const consumeMaterialIdentifier = this.getConsumeMaterialIdentifier(consumeMaterialItem);
        if (consumeMaterialCollectionIdentifiers.includes(consumeMaterialIdentifier)) {
          return false;
        }
        consumeMaterialCollectionIdentifiers.push(consumeMaterialIdentifier);
        return true;
      });
      return [...consumeMaterialsToAdd, ...consumeMaterialCollection];
    }
    return consumeMaterialCollection;
  }
}
