import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-favorite.test-samples';

import { UserFavoriteFormService } from './user-favorite-form.service';

describe('UserFavorite Form Service', () => {
  let service: UserFavoriteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserFavoriteFormService);
  });

  describe('Service methods', () => {
    describe('createUserFavoriteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserFavoriteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });

      it('passing IUserFavorite should create a new form with FormGroup', () => {
        const formGroup = service.createUserFavoriteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserFavorite', () => {
      it('should return NewUserFavorite for default UserFavorite initial value', () => {
        const formGroup = service.createUserFavoriteFormGroup(sampleWithNewData);

        const userFavorite = service.getUserFavorite(formGroup) as any;

        expect(userFavorite).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserFavorite for empty UserFavorite initial value', () => {
        const formGroup = service.createUserFavoriteFormGroup();

        const userFavorite = service.getUserFavorite(formGroup) as any;

        expect(userFavorite).toMatchObject({});
      });

      it('should return IUserFavorite', () => {
        const formGroup = service.createUserFavoriteFormGroup(sampleWithRequiredData);

        const userFavorite = service.getUserFavorite(formGroup) as any;

        expect(userFavorite).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserFavorite should not enable id FormControl', () => {
        const formGroup = service.createUserFavoriteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserFavorite should disable id FormControl', () => {
        const formGroup = service.createUserFavoriteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
