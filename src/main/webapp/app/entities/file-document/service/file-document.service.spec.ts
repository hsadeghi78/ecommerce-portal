import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFileDocument } from '../file-document.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../file-document.test-samples';

import { FileDocumentService } from './file-document.service';

const requireRestSample: IFileDocument = {
  ...sampleWithRequiredData,
};

describe('FileDocument Service', () => {
  let service: FileDocumentService;
  let httpMock: HttpTestingController;
  let expectedResult: IFileDocument | IFileDocument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FileDocumentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a FileDocument', () => {
      const fileDocument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fileDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FileDocument', () => {
      const fileDocument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fileDocument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FileDocument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FileDocument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FileDocument', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFileDocumentToCollectionIfMissing', () => {
      it('should add a FileDocument to an empty array', () => {
        const fileDocument: IFileDocument = sampleWithRequiredData;
        expectedResult = service.addFileDocumentToCollectionIfMissing([], fileDocument);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileDocument);
      });

      it('should not add a FileDocument to an array that contains it', () => {
        const fileDocument: IFileDocument = sampleWithRequiredData;
        const fileDocumentCollection: IFileDocument[] = [
          {
            ...fileDocument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFileDocumentToCollectionIfMissing(fileDocumentCollection, fileDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FileDocument to an array that doesn't contain it", () => {
        const fileDocument: IFileDocument = sampleWithRequiredData;
        const fileDocumentCollection: IFileDocument[] = [sampleWithPartialData];
        expectedResult = service.addFileDocumentToCollectionIfMissing(fileDocumentCollection, fileDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileDocument);
      });

      it('should add only unique FileDocument to an array', () => {
        const fileDocumentArray: IFileDocument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fileDocumentCollection: IFileDocument[] = [sampleWithRequiredData];
        expectedResult = service.addFileDocumentToCollectionIfMissing(fileDocumentCollection, ...fileDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fileDocument: IFileDocument = sampleWithRequiredData;
        const fileDocument2: IFileDocument = sampleWithPartialData;
        expectedResult = service.addFileDocumentToCollectionIfMissing([], fileDocument, fileDocument2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileDocument);
        expect(expectedResult).toContain(fileDocument2);
      });

      it('should accept null and undefined values', () => {
        const fileDocument: IFileDocument = sampleWithRequiredData;
        expectedResult = service.addFileDocumentToCollectionIfMissing([], null, fileDocument, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileDocument);
      });

      it('should return initial array if no FileDocument is added', () => {
        const fileDocumentCollection: IFileDocument[] = [sampleWithRequiredData];
        expectedResult = service.addFileDocumentToCollectionIfMissing(fileDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(fileDocumentCollection);
      });
    });

    describe('compareFileDocument', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFileDocument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFileDocument(entity1, entity2);
        const compareResult2 = service.compareFileDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFileDocument(entity1, entity2);
        const compareResult2 = service.compareFileDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFileDocument(entity1, entity2);
        const compareResult2 = service.compareFileDocument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
