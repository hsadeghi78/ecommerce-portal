import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductItem } from '../product-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../product-item.test-samples';

import { ProductItemService } from './product-item.service';

const requireRestSample: IProductItem = {
  ...sampleWithRequiredData,
};

describe('ProductItem Service', () => {
  let service: ProductItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductItem | IProductItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductItemService);
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

    it('should create a ProductItem', () => {
      const productItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductItem', () => {
      const productItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductItemToCollectionIfMissing', () => {
      it('should add a ProductItem to an empty array', () => {
        const productItem: IProductItem = sampleWithRequiredData;
        expectedResult = service.addProductItemToCollectionIfMissing([], productItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productItem);
      });

      it('should not add a ProductItem to an array that contains it', () => {
        const productItem: IProductItem = sampleWithRequiredData;
        const productItemCollection: IProductItem[] = [
          {
            ...productItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductItemToCollectionIfMissing(productItemCollection, productItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductItem to an array that doesn't contain it", () => {
        const productItem: IProductItem = sampleWithRequiredData;
        const productItemCollection: IProductItem[] = [sampleWithPartialData];
        expectedResult = service.addProductItemToCollectionIfMissing(productItemCollection, productItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productItem);
      });

      it('should add only unique ProductItem to an array', () => {
        const productItemArray: IProductItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productItemCollection: IProductItem[] = [sampleWithRequiredData];
        expectedResult = service.addProductItemToCollectionIfMissing(productItemCollection, ...productItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productItem: IProductItem = sampleWithRequiredData;
        const productItem2: IProductItem = sampleWithPartialData;
        expectedResult = service.addProductItemToCollectionIfMissing([], productItem, productItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productItem);
        expect(expectedResult).toContain(productItem2);
      });

      it('should accept null and undefined values', () => {
        const productItem: IProductItem = sampleWithRequiredData;
        expectedResult = service.addProductItemToCollectionIfMissing([], null, productItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productItem);
      });

      it('should return initial array if no ProductItem is added', () => {
        const productItemCollection: IProductItem[] = [sampleWithRequiredData];
        expectedResult = service.addProductItemToCollectionIfMissing(productItemCollection, undefined, null);
        expect(expectedResult).toEqual(productItemCollection);
      });
    });

    describe('compareProductItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProductItem(entity1, entity2);
        const compareResult2 = service.compareProductItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProductItem(entity1, entity2);
        const compareResult2 = service.compareProductItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProductItem(entity1, entity2);
        const compareResult2 = service.compareProductItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
