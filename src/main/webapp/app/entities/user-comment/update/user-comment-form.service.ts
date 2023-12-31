import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserComment, NewUserComment } from '../user-comment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserComment for edit and NewUserCommentFormGroupInput for create.
 */
type UserCommentFormGroupInput = IUserComment | PartialWithRequiredKeyOf<NewUserComment>;

type UserCommentFormDefaults = Pick<NewUserComment, 'id' | 'visible'>;

type UserCommentFormGroupContent = {
  id: FormControl<IUserComment['id'] | NewUserComment['id']>;
  rating: FormControl<IUserComment['rating']>;
  visible: FormControl<IUserComment['visible']>;
  description: FormControl<IUserComment['description']>;
  party: FormControl<IUserComment['party']>;
  product: FormControl<IUserComment['product']>;
  factor: FormControl<IUserComment['factor']>;
  parent: FormControl<IUserComment['parent']>;
};

export type UserCommentFormGroup = FormGroup<UserCommentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserCommentFormService {
  createUserCommentFormGroup(userComment: UserCommentFormGroupInput = { id: null }): UserCommentFormGroup {
    const userCommentRawValue = {
      ...this.getFormDefaults(),
      ...userComment,
    };
    return new FormGroup<UserCommentFormGroupContent>({
      id: new FormControl(
        { value: userCommentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      rating: new FormControl(userCommentRawValue.rating, {
        validators: [Validators.required],
      }),
      visible: new FormControl(userCommentRawValue.visible, {
        validators: [Validators.required],
      }),
      description: new FormControl(userCommentRawValue.description, {
        validators: [Validators.maxLength(2000)],
      }),
      party: new FormControl(userCommentRawValue.party),
      product: new FormControl(userCommentRawValue.product),
      factor: new FormControl(userCommentRawValue.factor),
      parent: new FormControl(userCommentRawValue.parent),
    });
  }

  getUserComment(form: UserCommentFormGroup): IUserComment | NewUserComment {
    return form.getRawValue() as IUserComment | NewUserComment;
  }

  resetForm(form: UserCommentFormGroup, userComment: UserCommentFormGroupInput): void {
    const userCommentRawValue = { ...this.getFormDefaults(), ...userComment };
    form.reset(
      {
        ...userCommentRawValue,
        id: { value: userCommentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserCommentFormDefaults {
    return {
      id: null,
      visible: false,
    };
  }
}
