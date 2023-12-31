import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserFavorite } from '../user-favorite.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../user-favorite.test-samples';

import { UserFavoriteService } from './user-favorite.service';

const requireRestSample: IUserFavorite = {
  ...sampleWithRequiredData,
};

describe('UserFavorite Service', () => {
  let service: UserFavoriteService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserFavorite | IUserFavorite[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserFavoriteService);
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

    it('should create a UserFavorite', () => {
      const userFavorite = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userFavorite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserFavorite', () => {
      const userFavorite = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userFavorite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserFavorite', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserFavorite', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserFavorite', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserFavoriteToCollectionIfMissing', () => {
      it('should add a UserFavorite to an empty array', () => {
        const userFavorite: IUserFavorite = sampleWithRequiredData;
        expectedResult = service.addUserFavoriteToCollectionIfMissing([], userFavorite);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userFavorite);
      });

      it('should not add a UserFavorite to an array that contains it', () => {
        const userFavorite: IUserFavorite = sampleWithRequiredData;
        const userFavoriteCollection: IUserFavorite[] = [
          {
            ...userFavorite,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserFavoriteToCollectionIfMissing(userFavoriteCollection, userFavorite);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserFavorite to an array that doesn't contain it", () => {
        const userFavorite: IUserFavorite = sampleWithRequiredData;
        const userFavoriteCollection: IUserFavorite[] = [sampleWithPartialData];
        expectedResult = service.addUserFavoriteToCollectionIfMissing(userFavoriteCollection, userFavorite);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userFavorite);
      });

      it('should add only unique UserFavorite to an array', () => {
        const userFavoriteArray: IUserFavorite[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userFavoriteCollection: IUserFavorite[] = [sampleWithRequiredData];
        expectedResult = service.addUserFavoriteToCollectionIfMissing(userFavoriteCollection, ...userFavoriteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userFavorite: IUserFavorite = sampleWithRequiredData;
        const userFavorite2: IUserFavorite = sampleWithPartialData;
        expectedResult = service.addUserFavoriteToCollectionIfMissing([], userFavorite, userFavorite2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userFavorite);
        expect(expectedResult).toContain(userFavorite2);
      });

      it('should accept null and undefined values', () => {
        const userFavorite: IUserFavorite = sampleWithRequiredData;
        expectedResult = service.addUserFavoriteToCollectionIfMissing([], null, userFavorite, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userFavorite);
      });

      it('should return initial array if no UserFavorite is added', () => {
        const userFavoriteCollection: IUserFavorite[] = [sampleWithRequiredData];
        expectedResult = service.addUserFavoriteToCollectionIfMissing(userFavoriteCollection, undefined, null);
        expect(expectedResult).toEqual(userFavoriteCollection);
      });
    });

    describe('compareUserFavorite', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserFavorite(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserFavorite(entity1, entity2);
        const compareResult2 = service.compareUserFavorite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserFavorite(entity1, entity2);
        const compareResult2 = service.compareUserFavorite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserFavorite(entity1, entity2);
        const compareResult2 = service.compareUserFavorite(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
