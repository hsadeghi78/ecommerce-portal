import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMyAuthority, NewMyAuthority } from '../my-authority.model';

export type PartialUpdateMyAuthority = Partial<IMyAuthority> & Pick<IMyAuthority, 'id'>;

export type EntityResponseType = HttpResponse<IMyAuthority>;
export type EntityArrayResponseType = HttpResponse<IMyAuthority[]>;

@Injectable({ providedIn: 'root' })
export class MyAuthorityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/my-authorities');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(myAuthority: NewMyAuthority): Observable<EntityResponseType> {
    return this.http.post<IMyAuthority>(this.resourceUrl, myAuthority, { observe: 'response' });
  }

  update(myAuthority: IMyAuthority): Observable<EntityResponseType> {
    return this.http.put<IMyAuthority>(`${this.resourceUrl}/${this.getMyAuthorityIdentifier(myAuthority)}`, myAuthority, {
      observe: 'response',
    });
  }

  partialUpdate(myAuthority: PartialUpdateMyAuthority): Observable<EntityResponseType> {
    return this.http.patch<IMyAuthority>(`${this.resourceUrl}/${this.getMyAuthorityIdentifier(myAuthority)}`, myAuthority, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMyAuthority>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMyAuthority[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMyAuthorityIdentifier(myAuthority: Pick<IMyAuthority, 'id'>): number {
    return myAuthority.id;
  }

  compareMyAuthority(o1: Pick<IMyAuthority, 'id'> | null, o2: Pick<IMyAuthority, 'id'> | null): boolean {
    return o1 && o2 ? this.getMyAuthorityIdentifier(o1) === this.getMyAuthorityIdentifier(o2) : o1 === o2;
  }

  addMyAuthorityToCollectionIfMissing<Type extends Pick<IMyAuthority, 'id'>>(
    myAuthorityCollection: Type[],
    ...myAuthoritiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const myAuthorities: Type[] = myAuthoritiesToCheck.filter(isPresent);
    if (myAuthorities.length > 0) {
      const myAuthorityCollectionIdentifiers = myAuthorityCollection.map(
        myAuthorityItem => this.getMyAuthorityIdentifier(myAuthorityItem)!,
      );
      const myAuthoritiesToAdd = myAuthorities.filter(myAuthorityItem => {
        const myAuthorityIdentifier = this.getMyAuthorityIdentifier(myAuthorityItem);
        if (myAuthorityCollectionIdentifiers.includes(myAuthorityIdentifier)) {
          return false;
        }
        myAuthorityCollectionIdentifiers.push(myAuthorityIdentifier);
        return true;
      });
      return [...myAuthoritiesToAdd, ...myAuthorityCollection];
    }
    return myAuthorityCollection;
  }
}
