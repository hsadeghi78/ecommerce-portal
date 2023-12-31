import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../geo-division.test-samples';

import { GeoDivisionFormService } from './geo-division-form.service';

describe('GeoDivision Form Service', () => {
  let service: GeoDivisionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GeoDivisionFormService);
  });

  describe('Service methods', () => {
    describe('createGeoDivisionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGeoDivisionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            code: expect.any(Object),
            level: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });

      it('passing IGeoDivision should create a new form with FormGroup', () => {
        const formGroup = service.createGeoDivisionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            code: expect.any(Object),
            level: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });
    });

    describe('getGeoDivision', () => {
      it('should return NewGeoDivision for default GeoDivision initial value', () => {
        const formGroup = service.createGeoDivisionFormGroup(sampleWithNewData);

        const geoDivision = service.getGeoDivision(formGroup) as any;

        expect(geoDivision).toMatchObject(sampleWithNewData);
      });

      it('should return NewGeoDivision for empty GeoDivision initial value', () => {
        const formGroup = service.createGeoDivisionFormGroup();

        const geoDivision = service.getGeoDivision(formGroup) as any;

        expect(geoDivision).toMatchObject({});
      });

      it('should return IGeoDivision', () => {
        const formGroup = service.createGeoDivisionFormGroup(sampleWithRequiredData);

        const geoDivision = service.getGeoDivision(formGroup) as any;

        expect(geoDivision).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGeoDivision should not enable id FormControl', () => {
        const formGroup = service.createGeoDivisionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGeoDivision should disable id FormControl', () => {
        const formGroup = service.createGeoDivisionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
