import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../factor-item.test-samples';

import { FactorItemFormService } from './factor-item-form.service';

describe('FactorItem Form Service', () => {
  let service: FactorItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FactorItemFormService);
  });

  describe('Service methods', () => {
    describe('createFactorItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFactorItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rowNum: expect.any(Object),
            title: expect.any(Object),
            count: expect.any(Object),
            discount: expect.any(Object),
            tax: expect.any(Object),
            description: expect.any(Object),
            factor: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });

      it('passing IFactorItem should create a new form with FormGroup', () => {
        const formGroup = service.createFactorItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rowNum: expect.any(Object),
            title: expect.any(Object),
            count: expect.any(Object),
            discount: expect.any(Object),
            tax: expect.any(Object),
            description: expect.any(Object),
            factor: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });
    });

    describe('getFactorItem', () => {
      it('should return NewFactorItem for default FactorItem initial value', () => {
        const formGroup = service.createFactorItemFormGroup(sampleWithNewData);

        const factorItem = service.getFactorItem(formGroup) as any;

        expect(factorItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewFactorItem for empty FactorItem initial value', () => {
        const formGroup = service.createFactorItemFormGroup();

        const factorItem = service.getFactorItem(formGroup) as any;

        expect(factorItem).toMatchObject({});
      });

      it('should return IFactorItem', () => {
        const formGroup = service.createFactorItemFormGroup(sampleWithRequiredData);

        const factorItem = service.getFactorItem(formGroup) as any;

        expect(factorItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFactorItem should not enable id FormControl', () => {
        const formGroup = service.createFactorItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFactorItem should disable id FormControl', () => {
        const formGroup = service.createFactorItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
