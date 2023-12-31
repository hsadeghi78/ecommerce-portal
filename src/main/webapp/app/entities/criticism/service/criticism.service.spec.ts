import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICriticism } from '../criticism.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../criticism.test-samples';

import { CriticismService } from './criticism.service';

const requireRestSample: ICriticism = {
  ...sampleWithRequiredData,
};

describe('Criticism Service', () => {
  let service: CriticismService;
  let httpMock: HttpTestingController;
  let expectedResult: ICriticism | ICriticism[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CriticismService);
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

    it('should create a Criticism', () => {
      const criticism = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(criticism).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Criticism', () => {
      const criticism = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(criticism).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Criticism', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Criticism', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Criticism', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCriticismToCollectionIfMissing', () => {
      it('should add a Criticism to an empty array', () => {
        const criticism: ICriticism = sampleWithRequiredData;
        expectedResult = service.addCriticismToCollectionIfMissing([], criticism);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(criticism);
      });

      it('should not add a Criticism to an array that contains it', () => {
        const criticism: ICriticism = sampleWithRequiredData;
        const criticismCollection: ICriticism[] = [
          {
            ...criticism,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCriticismToCollectionIfMissing(criticismCollection, criticism);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Criticism to an array that doesn't contain it", () => {
        const criticism: ICriticism = sampleWithRequiredData;
        const criticismCollection: ICriticism[] = [sampleWithPartialData];
        expectedResult = service.addCriticismToCollectionIfMissing(criticismCollection, criticism);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(criticism);
      });

      it('should add only unique Criticism to an array', () => {
        const criticismArray: ICriticism[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const criticismCollection: ICriticism[] = [sampleWithRequiredData];
        expectedResult = service.addCriticismToCollectionIfMissing(criticismCollection, ...criticismArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const criticism: ICriticism = sampleWithRequiredData;
        const criticism2: ICriticism = sampleWithPartialData;
        expectedResult = service.addCriticismToCollectionIfMissing([], criticism, criticism2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(criticism);
        expect(expectedResult).toContain(criticism2);
      });

      it('should accept null and undefined values', () => {
        const criticism: ICriticism = sampleWithRequiredData;
        expectedResult = service.addCriticismToCollectionIfMissing([], null, criticism, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(criticism);
      });

      it('should return initial array if no Criticism is added', () => {
        const criticismCollection: ICriticism[] = [sampleWithRequiredData];
        expectedResult = service.addCriticismToCollectionIfMissing(criticismCollection, undefined, null);
        expect(expectedResult).toEqual(criticismCollection);
      });
    });

    describe('compareCriticism', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCriticism(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCriticism(entity1, entity2);
        const compareResult2 = service.compareCriticism(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCriticism(entity1, entity2);
        const compareResult2 = service.compareCriticism(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCriticism(entity1, entity2);
        const compareResult2 = service.compareCriticism(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
