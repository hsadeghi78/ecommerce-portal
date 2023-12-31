import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../factor.test-samples';

import { FactorFormService } from './factor-form.service';

describe('Factor Form Service', () => {
  let service: FactorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FactorFormService);
  });

  describe('Service methods', () => {
    describe('createFactorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFactorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            factorCode: expect.any(Object),
            lastStatusClassId: expect.any(Object),
            paymentStateClassId: expect.any(Object),
            categoryClassId: expect.any(Object),
            totalPrice: expect.any(Object),
            discount: expect.any(Object),
            discountCode: expect.any(Object),
            finalTax: expect.any(Object),
            payable: expect.any(Object),
            description: expect.any(Object),
            location: expect.any(Object),
            buyerParty: expect.any(Object),
            sellerParty: expect.any(Object),
          }),
        );
      });

      it('passing IFactor should create a new form with FormGroup', () => {
        const formGroup = service.createFactorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            factorCode: expect.any(Object),
            lastStatusClassId: expect.any(Object),
            paymentStateClassId: expect.any(Object),
            categoryClassId: expect.any(Object),
            totalPrice: expect.any(Object),
            discount: expect.any(Object),
            discountCode: expect.any(Object),
            finalTax: expect.any(Object),
            payable: expect.any(Object),
            description: expect.any(Object),
            location: expect.any(Object),
            buyerParty: expect.any(Object),
            sellerParty: expect.any(Object),
          }),
        );
      });
    });

    describe('getFactor', () => {
      it('should return NewFactor for default Factor initial value', () => {
        const formGroup = service.createFactorFormGroup(sampleWithNewData);

        const factor = service.getFactor(formGroup) as any;

        expect(factor).toMatchObject(sampleWithNewData);
      });

      it('should return NewFactor for empty Factor initial value', () => {
        const formGroup = service.createFactorFormGroup();

        const factor = service.getFactor(formGroup) as any;

        expect(factor).toMatchObject({});
      });

      it('should return IFactor', () => {
        const formGroup = service.createFactorFormGroup(sampleWithRequiredData);

        const factor = service.getFactor(formGroup) as any;

        expect(factor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFactor should not enable id FormControl', () => {
        const formGroup = service.createFactorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFactor should disable id FormControl', () => {
        const formGroup = service.createFactorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
