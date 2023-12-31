import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../my-authority.test-samples';

import { MyAuthorityFormService } from './my-authority-form.service';

describe('MyAuthority Form Service', () => {
  let service: MyAuthorityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MyAuthorityFormService);
  });

  describe('Service methods', () => {
    describe('createMyAuthorityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMyAuthorityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            displayName: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });

      it('passing IMyAuthority should create a new form with FormGroup', () => {
        const formGroup = service.createMyAuthorityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            displayName: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });
    });

    describe('getMyAuthority', () => {
      it('should return NewMyAuthority for default MyAuthority initial value', () => {
        const formGroup = service.createMyAuthorityFormGroup(sampleWithNewData);

        const myAuthority = service.getMyAuthority(formGroup) as any;

        expect(myAuthority).toMatchObject(sampleWithNewData);
      });

      it('should return NewMyAuthority for empty MyAuthority initial value', () => {
        const formGroup = service.createMyAuthorityFormGroup();

        const myAuthority = service.getMyAuthority(formGroup) as any;

        expect(myAuthority).toMatchObject({});
      });

      it('should return IMyAuthority', () => {
        const formGroup = service.createMyAuthorityFormGroup(sampleWithRequiredData);

        const myAuthority = service.getMyAuthority(formGroup) as any;

        expect(myAuthority).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMyAuthority should not enable id FormControl', () => {
        const formGroup = service.createMyAuthorityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMyAuthority should disable id FormControl', () => {
        const formGroup = service.createMyAuthorityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
