import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../agreement.test-samples';

import { AgreementFormService } from './agreement-form.service';

describe('Agreement Form Service', () => {
  let service: AgreementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgreementFormService);
  });

  describe('Service methods', () => {
    describe('createAgreementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAgreementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            activationStatusClassId: expect.any(Object),
            infrastructureBenefit: expect.any(Object),
            extraBenefit: expect.any(Object),
            provider: expect.any(Object),
            consumer: expect.any(Object),
          }),
        );
      });

      it('passing IAgreement should create a new form with FormGroup', () => {
        const formGroup = service.createAgreementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            activationStatusClassId: expect.any(Object),
            infrastructureBenefit: expect.any(Object),
            extraBenefit: expect.any(Object),
            provider: expect.any(Object),
            consumer: expect.any(Object),
          }),
        );
      });
    });

    describe('getAgreement', () => {
      it('should return NewAgreement for default Agreement initial value', () => {
        const formGroup = service.createAgreementFormGroup(sampleWithNewData);

        const agreement = service.getAgreement(formGroup) as any;

        expect(agreement).toMatchObject(sampleWithNewData);
      });

      it('should return NewAgreement for empty Agreement initial value', () => {
        const formGroup = service.createAgreementFormGroup();

        const agreement = service.getAgreement(formGroup) as any;

        expect(agreement).toMatchObject({});
      });

      it('should return IAgreement', () => {
        const formGroup = service.createAgreementFormGroup(sampleWithRequiredData);

        const agreement = service.getAgreement(formGroup) as any;

        expect(agreement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAgreement should not enable id FormControl', () => {
        const formGroup = service.createAgreementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAgreement should disable id FormControl', () => {
        const formGroup = service.createAgreementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
