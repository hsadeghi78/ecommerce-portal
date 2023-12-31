import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../classification.test-samples';

import { ClassificationFormService } from './classification-form.service';

describe('Classification Form Service', () => {
  let service: ClassificationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClassificationFormService);
  });

  describe('Service methods', () => {
    describe('createClassificationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createClassificationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            classCode: expect.any(Object),
            description: expect.any(Object),
            languageClassId: expect.any(Object),
            classType: expect.any(Object),
          }),
        );
      });

      it('passing IClassification should create a new form with FormGroup', () => {
        const formGroup = service.createClassificationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            classCode: expect.any(Object),
            description: expect.any(Object),
            languageClassId: expect.any(Object),
            classType: expect.any(Object),
          }),
        );
      });
    });

    describe('getClassification', () => {
      it('should return NewClassification for default Classification initial value', () => {
        const formGroup = service.createClassificationFormGroup(sampleWithNewData);

        const classification = service.getClassification(formGroup) as any;

        expect(classification).toMatchObject(sampleWithNewData);
      });

      it('should return NewClassification for empty Classification initial value', () => {
        const formGroup = service.createClassificationFormGroup();

        const classification = service.getClassification(formGroup) as any;

        expect(classification).toMatchObject({});
      });

      it('should return IClassification', () => {
        const formGroup = service.createClassificationFormGroup(sampleWithRequiredData);

        const classification = service.getClassification(formGroup) as any;

        expect(classification).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IClassification should not enable id FormControl', () => {
        const formGroup = service.createClassificationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewClassification should disable id FormControl', () => {
        const formGroup = service.createClassificationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
