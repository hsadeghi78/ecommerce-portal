import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFileDocument, NewFileDocument } from '../file-document.model';

export type PartialUpdateFileDocument = Partial<IFileDocument> & Pick<IFileDocument, 'id'>;

export type EntityResponseType = HttpResponse<IFileDocument>;
export type EntityArrayResponseType = HttpResponse<IFileDocument[]>;

@Injectable({ providedIn: 'root' })
export class FileDocumentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/file-documents');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(fileDocument: NewFileDocument): Observable<EntityResponseType> {
    return this.http.post<IFileDocument>(this.resourceUrl, fileDocument, { observe: 'response' });
  }

  update(fileDocument: IFileDocument): Observable<EntityResponseType> {
    return this.http.put<IFileDocument>(`${this.resourceUrl}/${this.getFileDocumentIdentifier(fileDocument)}`, fileDocument, {
      observe: 'response',
    });
  }

  partialUpdate(fileDocument: PartialUpdateFileDocument): Observable<EntityResponseType> {
    return this.http.patch<IFileDocument>(`${this.resourceUrl}/${this.getFileDocumentIdentifier(fileDocument)}`, fileDocument, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFileDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFileDocument[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFileDocumentIdentifier(fileDocument: Pick<IFileDocument, 'id'>): number {
    return fileDocument.id;
  }

  compareFileDocument(o1: Pick<IFileDocument, 'id'> | null, o2: Pick<IFileDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getFileDocumentIdentifier(o1) === this.getFileDocumentIdentifier(o2) : o1 === o2;
  }

  addFileDocumentToCollectionIfMissing<Type extends Pick<IFileDocument, 'id'>>(
    fileDocumentCollection: Type[],
    ...fileDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fileDocuments: Type[] = fileDocumentsToCheck.filter(isPresent);
    if (fileDocuments.length > 0) {
      const fileDocumentCollectionIdentifiers = fileDocumentCollection.map(
        fileDocumentItem => this.getFileDocumentIdentifier(fileDocumentItem)!,
      );
      const fileDocumentsToAdd = fileDocuments.filter(fileDocumentItem => {
        const fileDocumentIdentifier = this.getFileDocumentIdentifier(fileDocumentItem);
        if (fileDocumentCollectionIdentifiers.includes(fileDocumentIdentifier)) {
          return false;
        }
        fileDocumentCollectionIdentifiers.push(fileDocumentIdentifier);
        return true;
      });
      return [...fileDocumentsToAdd, ...fileDocumentCollection];
    }
    return fileDocumentCollection;
  }
}
