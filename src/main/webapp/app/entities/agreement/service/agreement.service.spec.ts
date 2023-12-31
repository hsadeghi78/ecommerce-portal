import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAgreement } from '../agreement.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../agreement.test-samples';

import { AgreementService, RestAgreement } from './agreement.service';

const requireRestSample: RestAgreement = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('Agreement Service', () => {
  let service: AgreementService;
  let httpMock: HttpTestingController;
  let expectedResult: IAgreement | IAgreement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AgreementService);
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

    it('should create a Agreement', () => {
      const agreement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(agreement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Agreement', () => {
      const agreement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(agreement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Agreement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Agreement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Agreement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAgreementToCollectionIfMissing', () => {
      it('should add a Agreement to an empty array', () => {
        const agreement: IAgreement = sampleWithRequiredData;
        expectedResult = service.addAgreementToCollectionIfMissing([], agreement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agreement);
      });

      it('should not add a Agreement to an array that contains it', () => {
        const agreement: IAgreement = sampleWithRequiredData;
        const agreementCollection: IAgreement[] = [
          {
            ...agreement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAgreementToCollectionIfMissing(agreementCollection, agreement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Agreement to an array that doesn't contain it", () => {
        const agreement: IAgreement = sampleWithRequiredData;
        const agreementCollection: IAgreement[] = [sampleWithPartialData];
        expectedResult = service.addAgreementToCollectionIfMissing(agreementCollection, agreement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agreement);
      });

      it('should add only unique Agreement to an array', () => {
        const agreementArray: IAgreement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const agreementCollection: IAgreement[] = [sampleWithRequiredData];
        expectedResult = service.addAgreementToCollectionIfMissing(agreementCollection, ...agreementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const agreement: IAgreement = sampleWithRequiredData;
        const agreement2: IAgreement = sampleWithPartialData;
        expectedResult = service.addAgreementToCollectionIfMissing([], agreement, agreement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agreement);
        expect(expectedResult).toContain(agreement2);
      });

      it('should accept null and undefined values', () => {
        const agreement: IAgreement = sampleWithRequiredData;
        expectedResult = service.addAgreementToCollectionIfMissing([], null, agreement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agreement);
      });

      it('should return initial array if no Agreement is added', () => {
        const agreementCollection: IAgreement[] = [sampleWithRequiredData];
        expectedResult = service.addAgreementToCollectionIfMissing(agreementCollection, undefined, null);
        expect(expectedResult).toEqual(agreementCollection);
      });
    });

    describe('compareAgreement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAgreement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAgreement(entity1, entity2);
        const compareResult2 = service.compareAgreement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAgreement(entity1, entity2);
        const compareResult2 = service.compareAgreement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAgreement(entity1, entity2);
        const compareResult2 = service.compareAgreement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
