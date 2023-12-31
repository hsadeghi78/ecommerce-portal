import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassification, NewClassification } from '../classification.model';

export type PartialUpdateClassification = Partial<IClassification> & Pick<IClassification, 'id'>;

export type EntityResponseType = HttpResponse<IClassification>;
export type EntityArrayResponseType = HttpResponse<IClassification[]>;

@Injectable({ providedIn: 'root' })
export class ClassificationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/classifications');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(classification: NewClassification): Observable<EntityResponseType> {
    return this.http.post<IClassification>(this.resourceUrl, classification, { observe: 'response' });
  }

  update(classification: IClassification): Observable<EntityResponseType> {
    return this.http.put<IClassification>(`${this.resourceUrl}/${this.getClassificationIdentifier(classification)}`, classification, {
      observe: 'response',
    });
  }

  partialUpdate(classification: PartialUpdateClassification): Observable<EntityResponseType> {
    return this.http.patch<IClassification>(`${this.resourceUrl}/${this.getClassificationIdentifier(classification)}`, classification, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassification>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassification[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getClassificationIdentifier(classification: Pick<IClassification, 'id'>): number {
    return classification.id;
  }

  compareClassification(o1: Pick<IClassification, 'id'> | null, o2: Pick<IClassification, 'id'> | null): boolean {
    return o1 && o2 ? this.getClassificationIdentifier(o1) === this.getClassificationIdentifier(o2) : o1 === o2;
  }

  addClassificationToCollectionIfMissing<Type extends Pick<IClassification, 'id'>>(
    classificationCollection: Type[],
    ...classificationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const classifications: Type[] = classificationsToCheck.filter(isPresent);
    if (classifications.length > 0) {
      const classificationCollectionIdentifiers = classificationCollection.map(
        classificationItem => this.getClassificationIdentifier(classificationItem)!,
      );
      const classificationsToAdd = classifications.filter(classificationItem => {
        const classificationIdentifier = this.getClassificationIdentifier(classificationItem);
        if (classificationCollectionIdentifiers.includes(classificationIdentifier)) {
          return false;
        }
        classificationCollectionIdentifiers.push(classificationIdentifier);
        return true;
      });
      return [...classificationsToAdd, ...classificationCollection];
    }
    return classificationCollection;
  }
}
