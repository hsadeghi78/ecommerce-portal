import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserFavorite, NewUserFavorite } from '../user-favorite.model';

export type PartialUpdateUserFavorite = Partial<IUserFavorite> & Pick<IUserFavorite, 'id'>;

export type EntityResponseType = HttpResponse<IUserFavorite>;
export type EntityArrayResponseType = HttpResponse<IUserFavorite[]>;

@Injectable({ providedIn: 'root' })
export class UserFavoriteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-favorites');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(userFavorite: NewUserFavorite): Observable<EntityResponseType> {
    return this.http.post<IUserFavorite>(this.resourceUrl, userFavorite, { observe: 'response' });
  }

  update(userFavorite: IUserFavorite): Observable<EntityResponseType> {
    return this.http.put<IUserFavorite>(`${this.resourceUrl}/${this.getUserFavoriteIdentifier(userFavorite)}`, userFavorite, {
      observe: 'response',
    });
  }

  partialUpdate(userFavorite: PartialUpdateUserFavorite): Observable<EntityResponseType> {
    return this.http.patch<IUserFavorite>(`${this.resourceUrl}/${this.getUserFavoriteIdentifier(userFavorite)}`, userFavorite, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserFavorite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserFavorite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserFavoriteIdentifier(userFavorite: Pick<IUserFavorite, 'id'>): number {
    return userFavorite.id;
  }

  compareUserFavorite(o1: Pick<IUserFavorite, 'id'> | null, o2: Pick<IUserFavorite, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserFavoriteIdentifier(o1) === this.getUserFavoriteIdentifier(o2) : o1 === o2;
  }

  addUserFavoriteToCollectionIfMissing<Type extends Pick<IUserFavorite, 'id'>>(
    userFavoriteCollection: Type[],
    ...userFavoritesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userFavorites: Type[] = userFavoritesToCheck.filter(isPresent);
    if (userFavorites.length > 0) {
      const userFavoriteCollectionIdentifiers = userFavoriteCollection.map(
        userFavoriteItem => this.getUserFavoriteIdentifier(userFavoriteItem)!,
      );
      const userFavoritesToAdd = userFavorites.filter(userFavoriteItem => {
        const userFavoriteIdentifier = this.getUserFavoriteIdentifier(userFavoriteItem);
        if (userFavoriteCollectionIdentifiers.includes(userFavoriteIdentifier)) {
          return false;
        }
        userFavoriteCollectionIdentifiers.push(userFavoriteIdentifier);
        return true;
      });
      return [...userFavoritesToAdd, ...userFavoriteCollection];
    }
    return userFavoriteCollection;
  }
}
