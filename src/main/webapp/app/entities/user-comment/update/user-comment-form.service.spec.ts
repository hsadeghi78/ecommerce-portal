import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-comment.test-samples';

import { UserCommentFormService } from './user-comment-form.service';

describe('UserComment Form Service', () => {
  let service: UserCommentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserCommentFormService);
  });

  describe('Service methods', () => {
    describe('createUserCommentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserCommentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rating: expect.any(Object),
            visible: expect.any(Object),
            description: expect.any(Object),
            party: expect.any(Object),
            product: expect.any(Object),
            factor: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });

      it('passing IUserComment should create a new form with FormGroup', () => {
        const formGroup = service.createUserCommentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rating: expect.any(Object),
            visible: expect.any(Object),
            description: expect.any(Object),
            party: expect.any(Object),
            product: expect.any(Object),
            factor: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserComment', () => {
      it('should return NewUserComment for default UserComment initial value', () => {
        const formGroup = service.createUserCommentFormGroup(sampleWithNewData);

        const userComment = service.getUserComment(formGroup) as any;

        expect(userComment).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserComment for empty UserComment initial value', () => {
        const formGroup = service.createUserCommentFormGroup();

        const userComment = service.getUserComment(formGroup) as any;

        expect(userComment).toMatchObject({});
      });

      it('should return IUserComment', () => {
        const formGroup = service.createUserCommentFormGroup(sampleWithRequiredData);

        const userComment = service.getUserComment(formGroup) as any;

        expect(userComment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserComment should not enable id FormControl', () => {
        const formGroup = service.createUserCommentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserComment should disable id FormControl', () => {
        const formGroup = service.createUserCommentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
