import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserComment, NewUserComment } from '../user-comment.model';

export type PartialUpdateUserComment = Partial<IUserComment> & Pick<IUserComment, 'id'>;

export type EntityResponseType = HttpResponse<IUserComment>;
export type EntityArrayResponseType = HttpResponse<IUserComment[]>;

@Injectable({ providedIn: 'root' })
export class UserCommentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-comments');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(userComment: NewUserComment): Observable<EntityResponseType> {
    return this.http.post<IUserComment>(this.resourceUrl, userComment, { observe: 'response' });
  }

  update(userComment: IUserComment): Observable<EntityResponseType> {
    return this.http.put<IUserComment>(`${this.resourceUrl}/${this.getUserCommentIdentifier(userComment)}`, userComment, {
      observe: 'response',
    });
  }

  partialUpdate(userComment: PartialUpdateUserComment): Observable<EntityResponseType> {
    return this.http.patch<IUserComment>(`${this.resourceUrl}/${this.getUserCommentIdentifier(userComment)}`, userComment, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserComment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserComment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserCommentIdentifier(userComment: Pick<IUserComment, 'id'>): number {
    return userComment.id;
  }

  compareUserComment(o1: Pick<IUserComment, 'id'> | null, o2: Pick<IUserComment, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserCommentIdentifier(o1) === this.getUserCommentIdentifier(o2) : o1 === o2;
  }

  addUserCommentToCollectionIfMissing<Type extends Pick<IUserComment, 'id'>>(
    userCommentCollection: Type[],
    ...userCommentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userComments: Type[] = userCommentsToCheck.filter(isPresent);
    if (userComments.length > 0) {
      const userCommentCollectionIdentifiers = userCommentCollection.map(
        userCommentItem => this.getUserCommentIdentifier(userCommentItem)!,
      );
      const userCommentsToAdd = userComments.filter(userCommentItem => {
        const userCommentIdentifier = this.getUserCommentIdentifier(userCommentItem);
        if (userCommentCollectionIdentifiers.includes(userCommentIdentifier)) {
          return false;
        }
        userCommentCollectionIdentifiers.push(userCommentIdentifier);
        return true;
      });
      return [...userCommentsToAdd, ...userCommentCollection];
    }
    return userCommentCollection;
  }
}
