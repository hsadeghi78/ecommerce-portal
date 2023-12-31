import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../consume-material.test-samples';

import { ConsumeMaterialFormService } from './consume-material-form.service';

describe('ConsumeMaterial Form Service', () => {
  let service: ConsumeMaterialFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConsumeMaterialFormService);
  });

  describe('Service methods', () => {
    describe('createConsumeMaterialFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConsumeMaterialFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeClassId: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });

      it('passing IConsumeMaterial should create a new form with FormGroup', () => {
        const formGroup = service.createConsumeMaterialFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeClassId: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });
    });

    describe('getConsumeMaterial', () => {
      it('should return NewConsumeMaterial for default ConsumeMaterial initial value', () => {
        const formGroup = service.createConsumeMaterialFormGroup(sampleWithNewData);

        const consumeMaterial = service.getConsumeMaterial(formGroup) as any;

        expect(consumeMaterial).toMatchObject(sampleWithNewData);
      });

      it('should return NewConsumeMaterial for empty ConsumeMaterial initial value', () => {
        const formGroup = service.createConsumeMaterialFormGroup();

        const consumeMaterial = service.getConsumeMaterial(formGroup) as any;

        expect(consumeMaterial).toMatchObject({});
      });

      it('should return IConsumeMaterial', () => {
        const formGroup = service.createConsumeMaterialFormGroup(sampleWithRequiredData);

        const consumeMaterial = service.getConsumeMaterial(formGroup) as any;

        expect(consumeMaterial).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConsumeMaterial should not enable id FormControl', () => {
        const formGroup = service.createConsumeMaterialFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConsumeMaterial should disable id FormControl', () => {
        const formGroup = service.createConsumeMaterialFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
