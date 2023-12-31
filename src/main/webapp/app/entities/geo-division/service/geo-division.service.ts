import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGeoDivision, NewGeoDivision } from '../geo-division.model';

export type PartialUpdateGeoDivision = Partial<IGeoDivision> & Pick<IGeoDivision, 'id'>;

export type EntityResponseType = HttpResponse<IGeoDivision>;
export type EntityArrayResponseType = HttpResponse<IGeoDivision[]>;

@Injectable({ providedIn: 'root' })
export class GeoDivisionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/geo-divisions');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(geoDivision: NewGeoDivision): Observable<EntityResponseType> {
    return this.http.post<IGeoDivision>(this.resourceUrl, geoDivision, { observe: 'response' });
  }

  update(geoDivision: IGeoDivision): Observable<EntityResponseType> {
    return this.http.put<IGeoDivision>(`${this.resourceUrl}/${this.getGeoDivisionIdentifier(geoDivision)}`, geoDivision, {
      observe: 'response',
    });
  }

  partialUpdate(geoDivision: PartialUpdateGeoDivision): Observable<EntityResponseType> {
    return this.http.patch<IGeoDivision>(`${this.resourceUrl}/${this.getGeoDivisionIdentifier(geoDivision)}`, geoDivision, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGeoDivision>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGeoDivision[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGeoDivisionIdentifier(geoDivision: Pick<IGeoDivision, 'id'>): number {
    return geoDivision.id;
  }

  compareGeoDivision(o1: Pick<IGeoDivision, 'id'> | null, o2: Pick<IGeoDivision, 'id'> | null): boolean {
    return o1 && o2 ? this.getGeoDivisionIdentifier(o1) === this.getGeoDivisionIdentifier(o2) : o1 === o2;
  }

  addGeoDivisionToCollectionIfMissing<Type extends Pick<IGeoDivision, 'id'>>(
    geoDivisionCollection: Type[],
    ...geoDivisionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const geoDivisions: Type[] = geoDivisionsToCheck.filter(isPresent);
    if (geoDivisions.length > 0) {
      const geoDivisionCollectionIdentifiers = geoDivisionCollection.map(
        geoDivisionItem => this.getGeoDivisionIdentifier(geoDivisionItem)!,
      );
      const geoDivisionsToAdd = geoDivisions.filter(geoDivisionItem => {
        const geoDivisionIdentifier = this.getGeoDivisionIdentifier(geoDivisionItem);
        if (geoDivisionCollectionIdentifiers.includes(geoDivisionIdentifier)) {
          return false;
        }
        geoDivisionCollectionIdentifiers.push(geoDivisionIdentifier);
        return true;
      });
      return [...geoDivisionsToAdd, ...geoDivisionCollection];
    }
    return geoDivisionCollection;
  }
}
