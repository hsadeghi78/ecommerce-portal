import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../criticism.test-samples';

import { CriticismFormService } from './criticism-form.service';

describe('Criticism Form Service', () => {
  let service: CriticismFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CriticismFormService);
  });

  describe('Service methods', () => {
    describe('createCriticismFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCriticismFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fullName: expect.any(Object),
            email: expect.any(Object),
            contactNumber: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing ICriticism should create a new form with FormGroup', () => {
        const formGroup = service.createCriticismFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fullName: expect.any(Object),
            email: expect.any(Object),
            contactNumber: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getCriticism', () => {
      it('should return NewCriticism for default Criticism initial value', () => {
        const formGroup = service.createCriticismFormGroup(sampleWithNewData);

        const criticism = service.getCriticism(formGroup) as any;

        expect(criticism).toMatchObject(sampleWithNewData);
      });

      it('should return NewCriticism for empty Criticism initial value', () => {
        const formGroup = service.createCriticismFormGroup();

        const criticism = service.getCriticism(formGroup) as any;

        expect(criticism).toMatchObject({});
      });

      it('should return ICriticism', () => {
        const formGroup = service.createCriticismFormGroup(sampleWithRequiredData);

        const criticism = service.getCriticism(formGroup) as any;

        expect(criticism).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICriticism should not enable id FormControl', () => {
        const formGroup = service.createCriticismFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCriticism should disable id FormControl', () => {
        const formGroup = service.createCriticismFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
