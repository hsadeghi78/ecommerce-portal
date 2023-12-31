import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMyAuthority } from '../my-authority.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../my-authority.test-samples';

import { MyAuthorityService } from './my-authority.service';

const requireRestSample: IMyAuthority = {
  ...sampleWithRequiredData,
};

describe('MyAuthority Service', () => {
  let service: MyAuthorityService;
  let httpMock: HttpTestingController;
  let expectedResult: IMyAuthority | IMyAuthority[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MyAuthorityService);
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

    it('should create a MyAuthority', () => {
      const myAuthority = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(myAuthority).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MyAuthority', () => {
      const myAuthority = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(myAuthority).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MyAuthority', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MyAuthority', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MyAuthority', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMyAuthorityToCollectionIfMissing', () => {
      it('should add a MyAuthority to an empty array', () => {
        const myAuthority: IMyAuthority = sampleWithRequiredData;
        expectedResult = service.addMyAuthorityToCollectionIfMissing([], myAuthority);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(myAuthority);
      });

      it('should not add a MyAuthority to an array that contains it', () => {
        const myAuthority: IMyAuthority = sampleWithRequiredData;
        const myAuthorityCollection: IMyAuthority[] = [
          {
            ...myAuthority,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMyAuthorityToCollectionIfMissing(myAuthorityCollection, myAuthority);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MyAuthority to an array that doesn't contain it", () => {
        const myAuthority: IMyAuthority = sampleWithRequiredData;
        const myAuthorityCollection: IMyAuthority[] = [sampleWithPartialData];
        expectedResult = service.addMyAuthorityToCollectionIfMissing(myAuthorityCollection, myAuthority);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(myAuthority);
      });

      it('should add only unique MyAuthority to an array', () => {
        const myAuthorityArray: IMyAuthority[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const myAuthorityCollection: IMyAuthority[] = [sampleWithRequiredData];
        expectedResult = service.addMyAuthorityToCollectionIfMissing(myAuthorityCollection, ...myAuthorityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const myAuthority: IMyAuthority = sampleWithRequiredData;
        const myAuthority2: IMyAuthority = sampleWithPartialData;
        expectedResult = service.addMyAuthorityToCollectionIfMissing([], myAuthority, myAuthority2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(myAuthority);
        expect(expectedResult).toContain(myAuthority2);
      });

      it('should accept null and undefined values', () => {
        const myAuthority: IMyAuthority = sampleWithRequiredData;
        expectedResult = service.addMyAuthorityToCollectionIfMissing([], null, myAuthority, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(myAuthority);
      });

      it('should return initial array if no MyAuthority is added', () => {
        const myAuthorityCollection: IMyAuthority[] = [sampleWithRequiredData];
        expectedResult = service.addMyAuthorityToCollectionIfMissing(myAuthorityCollection, undefined, null);
        expect(expectedResult).toEqual(myAuthorityCollection);
      });
    });

    describe('compareMyAuthority', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMyAuthority(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMyAuthority(entity1, entity2);
        const compareResult2 = service.compareMyAuthority(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMyAuthority(entity1, entity2);
        const compareResult2 = service.compareMyAuthority(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMyAuthority(entity1, entity2);
        const compareResult2 = service.compareMyAuthority(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
