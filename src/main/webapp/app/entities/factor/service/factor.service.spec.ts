import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFactor } from '../factor.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../factor.test-samples';

import { FactorService } from './factor.service';

const requireRestSample: IFactor = {
  ...sampleWithRequiredData,
};

describe('Factor Service', () => {
  let service: FactorService;
  let httpMock: HttpTestingController;
  let expectedResult: IFactor | IFactor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FactorService);
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

    it('should create a Factor', () => {
      const factor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(factor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Factor', () => {
      const factor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(factor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Factor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Factor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Factor', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFactorToCollectionIfMissing', () => {
      it('should add a Factor to an empty array', () => {
        const factor: IFactor = sampleWithRequiredData;
        expectedResult = service.addFactorToCollectionIfMissing([], factor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factor);
      });

      it('should not add a Factor to an array that contains it', () => {
        const factor: IFactor = sampleWithRequiredData;
        const factorCollection: IFactor[] = [
          {
            ...factor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFactorToCollectionIfMissing(factorCollection, factor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Factor to an array that doesn't contain it", () => {
        const factor: IFactor = sampleWithRequiredData;
        const factorCollection: IFactor[] = [sampleWithPartialData];
        expectedResult = service.addFactorToCollectionIfMissing(factorCollection, factor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factor);
      });

      it('should add only unique Factor to an array', () => {
        const factorArray: IFactor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const factorCollection: IFactor[] = [sampleWithRequiredData];
        expectedResult = service.addFactorToCollectionIfMissing(factorCollection, ...factorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factor: IFactor = sampleWithRequiredData;
        const factor2: IFactor = sampleWithPartialData;
        expectedResult = service.addFactorToCollectionIfMissing([], factor, factor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factor);
        expect(expectedResult).toContain(factor2);
      });

      it('should accept null and undefined values', () => {
        const factor: IFactor = sampleWithRequiredData;
        expectedResult = service.addFactorToCollectionIfMissing([], null, factor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factor);
      });

      it('should return initial array if no Factor is added', () => {
        const factorCollection: IFactor[] = [sampleWithRequiredData];
        expectedResult = service.addFactorToCollectionIfMissing(factorCollection, undefined, null);
        expect(expectedResult).toEqual(factorCollection);
      });
    });

    describe('compareFactor', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFactor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFactor(entity1, entity2);
        const compareResult2 = service.compareFactor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFactor(entity1, entity2);
        const compareResult2 = service.compareFactor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFactor(entity1, entity2);
        const compareResult2 = service.compareFactor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
