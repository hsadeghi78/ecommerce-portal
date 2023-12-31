import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClassification } from '../classification.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../classification.test-samples';

import { ClassificationService } from './classification.service';

const requireRestSample: IClassification = {
  ...sampleWithRequiredData,
};

describe('Classification Service', () => {
  let service: ClassificationService;
  let httpMock: HttpTestingController;
  let expectedResult: IClassification | IClassification[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ClassificationService);
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

    it('should create a Classification', () => {
      const classification = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(classification).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Classification', () => {
      const classification = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(classification).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Classification', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Classification', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Classification', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addClassificationToCollectionIfMissing', () => {
      it('should add a Classification to an empty array', () => {
        const classification: IClassification = sampleWithRequiredData;
        expectedResult = service.addClassificationToCollectionIfMissing([], classification);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classification);
      });

      it('should not add a Classification to an array that contains it', () => {
        const classification: IClassification = sampleWithRequiredData;
        const classificationCollection: IClassification[] = [
          {
            ...classification,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addClassificationToCollectionIfMissing(classificationCollection, classification);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Classification to an array that doesn't contain it", () => {
        const classification: IClassification = sampleWithRequiredData;
        const classificationCollection: IClassification[] = [sampleWithPartialData];
        expectedResult = service.addClassificationToCollectionIfMissing(classificationCollection, classification);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classification);
      });

      it('should add only unique Classification to an array', () => {
        const classificationArray: IClassification[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const classificationCollection: IClassification[] = [sampleWithRequiredData];
        expectedResult = service.addClassificationToCollectionIfMissing(classificationCollection, ...classificationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const classification: IClassification = sampleWithRequiredData;
        const classification2: IClassification = sampleWithPartialData;
        expectedResult = service.addClassificationToCollectionIfMissing([], classification, classification2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classification);
        expect(expectedResult).toContain(classification2);
      });

      it('should accept null and undefined values', () => {
        const classification: IClassification = sampleWithRequiredData;
        expectedResult = service.addClassificationToCollectionIfMissing([], null, classification, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classification);
      });

      it('should return initial array if no Classification is added', () => {
        const classificationCollection: IClassification[] = [sampleWithRequiredData];
        expectedResult = service.addClassificationToCollectionIfMissing(classificationCollection, undefined, null);
        expect(expectedResult).toEqual(classificationCollection);
      });
    });

    describe('compareClassification', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareClassification(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareClassification(entity1, entity2);
        const compareResult2 = service.compareClassification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareClassification(entity1, entity2);
        const compareResult2 = service.compareClassification(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareClassification(entity1, entity2);
        const compareResult2 = service.compareClassification(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
