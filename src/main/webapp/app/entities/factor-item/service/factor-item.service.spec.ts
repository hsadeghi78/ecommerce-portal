import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFactorItem } from '../factor-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../factor-item.test-samples';

import { FactorItemService } from './factor-item.service';

const requireRestSample: IFactorItem = {
  ...sampleWithRequiredData,
};

describe('FactorItem Service', () => {
  let service: FactorItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IFactorItem | IFactorItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FactorItemService);
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

    it('should create a FactorItem', () => {
      const factorItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(factorItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FactorItem', () => {
      const factorItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(factorItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FactorItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FactorItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FactorItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFactorItemToCollectionIfMissing', () => {
      it('should add a FactorItem to an empty array', () => {
        const factorItem: IFactorItem = sampleWithRequiredData;
        expectedResult = service.addFactorItemToCollectionIfMissing([], factorItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factorItem);
      });

      it('should not add a FactorItem to an array that contains it', () => {
        const factorItem: IFactorItem = sampleWithRequiredData;
        const factorItemCollection: IFactorItem[] = [
          {
            ...factorItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFactorItemToCollectionIfMissing(factorItemCollection, factorItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FactorItem to an array that doesn't contain it", () => {
        const factorItem: IFactorItem = sampleWithRequiredData;
        const factorItemCollection: IFactorItem[] = [sampleWithPartialData];
        expectedResult = service.addFactorItemToCollectionIfMissing(factorItemCollection, factorItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factorItem);
      });

      it('should add only unique FactorItem to an array', () => {
        const factorItemArray: IFactorItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const factorItemCollection: IFactorItem[] = [sampleWithRequiredData];
        expectedResult = service.addFactorItemToCollectionIfMissing(factorItemCollection, ...factorItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factorItem: IFactorItem = sampleWithRequiredData;
        const factorItem2: IFactorItem = sampleWithPartialData;
        expectedResult = service.addFactorItemToCollectionIfMissing([], factorItem, factorItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factorItem);
        expect(expectedResult).toContain(factorItem2);
      });

      it('should accept null and undefined values', () => {
        const factorItem: IFactorItem = sampleWithRequiredData;
        expectedResult = service.addFactorItemToCollectionIfMissing([], null, factorItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factorItem);
      });

      it('should return initial array if no FactorItem is added', () => {
        const factorItemCollection: IFactorItem[] = [sampleWithRequiredData];
        expectedResult = service.addFactorItemToCollectionIfMissing(factorItemCollection, undefined, null);
        expect(expectedResult).toEqual(factorItemCollection);
      });
    });

    describe('compareFactorItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFactorItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFactorItem(entity1, entity2);
        const compareResult2 = service.compareFactorItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFactorItem(entity1, entity2);
        const compareResult2 = service.compareFactorItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFactorItem(entity1, entity2);
        const compareResult2 = service.compareFactorItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
