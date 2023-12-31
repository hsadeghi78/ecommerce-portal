import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICampaign } from '../campaign.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../campaign.test-samples';

import { CampaignService, RestCampaign } from './campaign.service';

const requireRestSample: RestCampaign = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('Campaign Service', () => {
  let service: CampaignService;
  let httpMock: HttpTestingController;
  let expectedResult: ICampaign | ICampaign[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CampaignService);
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

    it('should create a Campaign', () => {
      const campaign = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(campaign).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Campaign', () => {
      const campaign = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(campaign).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Campaign', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Campaign', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Campaign', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCampaignToCollectionIfMissing', () => {
      it('should add a Campaign to an empty array', () => {
        const campaign: ICampaign = sampleWithRequiredData;
        expectedResult = service.addCampaignToCollectionIfMissing([], campaign);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(campaign);
      });

      it('should not add a Campaign to an array that contains it', () => {
        const campaign: ICampaign = sampleWithRequiredData;
        const campaignCollection: ICampaign[] = [
          {
            ...campaign,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCampaignToCollectionIfMissing(campaignCollection, campaign);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Campaign to an array that doesn't contain it", () => {
        const campaign: ICampaign = sampleWithRequiredData;
        const campaignCollection: ICampaign[] = [sampleWithPartialData];
        expectedResult = service.addCampaignToCollectionIfMissing(campaignCollection, campaign);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(campaign);
      });

      it('should add only unique Campaign to an array', () => {
        const campaignArray: ICampaign[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const campaignCollection: ICampaign[] = [sampleWithRequiredData];
        expectedResult = service.addCampaignToCollectionIfMissing(campaignCollection, ...campaignArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const campaign: ICampaign = sampleWithRequiredData;
        const campaign2: ICampaign = sampleWithPartialData;
        expectedResult = service.addCampaignToCollectionIfMissing([], campaign, campaign2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(campaign);
        expect(expectedResult).toContain(campaign2);
      });

      it('should accept null and undefined values', () => {
        const campaign: ICampaign = sampleWithRequiredData;
        expectedResult = service.addCampaignToCollectionIfMissing([], null, campaign, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(campaign);
      });

      it('should return initial array if no Campaign is added', () => {
        const campaignCollection: ICampaign[] = [sampleWithRequiredData];
        expectedResult = service.addCampaignToCollectionIfMissing(campaignCollection, undefined, null);
        expect(expectedResult).toEqual(campaignCollection);
      });
    });

    describe('compareCampaign', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCampaign(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCampaign(entity1, entity2);
        const compareResult2 = service.compareCampaign(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCampaign(entity1, entity2);
        const compareResult2 = service.compareCampaign(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCampaign(entity1, entity2);
        const compareResult2 = service.compareCampaign(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
