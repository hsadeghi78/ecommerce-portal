import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../campaign.test-samples';

import { CampaignFormService } from './campaign-form.service';

describe('Campaign Form Service', () => {
  let service: CampaignFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CampaignFormService);
  });

  describe('Service methods', () => {
    describe('createCampaignFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCampaignFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            photo: expect.any(Object),
            description: expect.any(Object),
            products: expect.any(Object),
          }),
        );
      });

      it('passing ICampaign should create a new form with FormGroup', () => {
        const formGroup = service.createCampaignFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            photo: expect.any(Object),
            description: expect.any(Object),
            products: expect.any(Object),
          }),
        );
      });
    });

    describe('getCampaign', () => {
      it('should return NewCampaign for default Campaign initial value', () => {
        const formGroup = service.createCampaignFormGroup(sampleWithNewData);

        const campaign = service.getCampaign(formGroup) as any;

        expect(campaign).toMatchObject(sampleWithNewData);
      });

      it('should return NewCampaign for empty Campaign initial value', () => {
        const formGroup = service.createCampaignFormGroup();

        const campaign = service.getCampaign(formGroup) as any;

        expect(campaign).toMatchObject({});
      });

      it('should return ICampaign', () => {
        const formGroup = service.createCampaignFormGroup(sampleWithRequiredData);

        const campaign = service.getCampaign(formGroup) as any;

        expect(campaign).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICampaign should not enable id FormControl', () => {
        const formGroup = service.createCampaignFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCampaign should disable id FormControl', () => {
        const formGroup = service.createCampaignFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
