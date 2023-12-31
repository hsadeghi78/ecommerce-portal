import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICriticism, NewCriticism } from '../criticism.model';

export type PartialUpdateCriticism = Partial<ICriticism> & Pick<ICriticism, 'id'>;

export type EntityResponseType = HttpResponse<ICriticism>;
export type EntityArrayResponseType = HttpResponse<ICriticism[]>;

@Injectable({ providedIn: 'root' })
export class CriticismService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/criticisms');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(criticism: NewCriticism): Observable<EntityResponseType> {
    return this.http.post<ICriticism>(this.resourceUrl, criticism, { observe: 'response' });
  }

  update(criticism: ICriticism): Observable<EntityResponseType> {
    return this.http.put<ICriticism>(`${this.resourceUrl}/${this.getCriticismIdentifier(criticism)}`, criticism, { observe: 'response' });
  }

  partialUpdate(criticism: PartialUpdateCriticism): Observable<EntityResponseType> {
    return this.http.patch<ICriticism>(`${this.resourceUrl}/${this.getCriticismIdentifier(criticism)}`, criticism, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICriticism>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICriticism[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCriticismIdentifier(criticism: Pick<ICriticism, 'id'>): number {
    return criticism.id;
  }

  compareCriticism(o1: Pick<ICriticism, 'id'> | null, o2: Pick<ICriticism, 'id'> | null): boolean {
    return o1 && o2 ? this.getCriticismIdentifier(o1) === this.getCriticismIdentifier(o2) : o1 === o2;
  }

  addCriticismToCollectionIfMissing<Type extends Pick<ICriticism, 'id'>>(
    criticismCollection: Type[],
    ...criticismsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const criticisms: Type[] = criticismsToCheck.filter(isPresent);
    if (criticisms.length > 0) {
      const criticismCollectionIdentifiers = criticismCollection.map(criticismItem => this.getCriticismIdentifier(criticismItem)!);
      const criticismsToAdd = criticisms.filter(criticismItem => {
        const criticismIdentifier = this.getCriticismIdentifier(criticismItem);
        if (criticismCollectionIdentifiers.includes(criticismIdentifier)) {
          return false;
        }
        criticismCollectionIdentifiers.push(criticismIdentifier);
        return true;
      });
      return [...criticismsToAdd, ...criticismCollection];
    }
    return criticismCollection;
  }
}
