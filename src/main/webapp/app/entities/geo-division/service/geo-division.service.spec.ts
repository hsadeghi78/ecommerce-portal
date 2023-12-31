import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGeoDivision } from '../geo-division.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../geo-division.test-samples';

import { GeoDivisionService } from './geo-division.service';

const requireRestSample: IGeoDivision = {
  ...sampleWithRequiredData,
};

describe('GeoDivision Service', () => {
  let service: GeoDivisionService;
  let httpMock: HttpTestingController;
  let expectedResult: IGeoDivision | IGeoDivision[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GeoDivisionService);
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

    it('should create a GeoDivision', () => {
      const geoDivision = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(geoDivision).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GeoDivision', () => {
      const geoDivision = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(geoDivision).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GeoDivision', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GeoDivision', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GeoDivision', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGeoDivisionToCollectionIfMissing', () => {
      it('should add a GeoDivision to an empty array', () => {
        const geoDivision: IGeoDivision = sampleWithRequiredData;
        expectedResult = service.addGeoDivisionToCollectionIfMissing([], geoDivision);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(geoDivision);
      });

      it('should not add a GeoDivision to an array that contains it', () => {
        const geoDivision: IGeoDivision = sampleWithRequiredData;
        const geoDivisionCollection: IGeoDivision[] = [
          {
            ...geoDivision,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGeoDivisionToCollectionIfMissing(geoDivisionCollection, geoDivision);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GeoDivision to an array that doesn't contain it", () => {
        const geoDivision: IGeoDivision = sampleWithRequiredData;
        const geoDivisionCollection: IGeoDivision[] = [sampleWithPartialData];
        expectedResult = service.addGeoDivisionToCollectionIfMissing(geoDivisionCollection, geoDivision);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(geoDivision);
      });

      it('should add only unique GeoDivision to an array', () => {
        const geoDivisionArray: IGeoDivision[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const geoDivisionCollection: IGeoDivision[] = [sampleWithRequiredData];
        expectedResult = service.addGeoDivisionToCollectionIfMissing(geoDivisionCollection, ...geoDivisionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const geoDivision: IGeoDivision = sampleWithRequiredData;
        const geoDivision2: IGeoDivision = sampleWithPartialData;
        expectedResult = service.addGeoDivisionToCollectionIfMissing([], geoDivision, geoDivision2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(geoDivision);
        expect(expectedResult).toContain(geoDivision2);
      });

      it('should accept null and undefined values', () => {
        const geoDivision: IGeoDivision = sampleWithRequiredData;
        expectedResult = service.addGeoDivisionToCollectionIfMissing([], null, geoDivision, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(geoDivision);
      });

      it('should return initial array if no GeoDivision is added', () => {
        const geoDivisionCollection: IGeoDivision[] = [sampleWithRequiredData];
        expectedResult = service.addGeoDivisionToCollectionIfMissing(geoDivisionCollection, undefined, null);
        expect(expectedResult).toEqual(geoDivisionCollection);
      });
    });

    describe('compareGeoDivision', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGeoDivision(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGeoDivision(entity1, entity2);
        const compareResult2 = service.compareGeoDivision(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGeoDivision(entity1, entity2);
        const compareResult2 = service.compareGeoDivision(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGeoDivision(entity1, entity2);
        const compareResult2 = service.compareGeoDivision(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
