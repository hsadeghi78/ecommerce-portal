import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserFavorite, NewUserFavorite } from '../user-favorite.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserFavorite for edit and NewUserFavoriteFormGroupInput for create.
 */
type UserFavoriteFormGroupInput = IUserFavorite | PartialWithRequiredKeyOf<NewUserFavorite>;

type UserFavoriteFormDefaults = Pick<NewUserFavorite, 'id'>;

type UserFavoriteFormGroupContent = {
  id: FormControl<IUserFavorite['id'] | NewUserFavorite['id']>;
  product: FormControl<IUserFavorite['product']>;
};

export type UserFavoriteFormGroup = FormGroup<UserFavoriteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserFavoriteFormService {
  createUserFavoriteFormGroup(userFavorite: UserFavoriteFormGroupInput = { id: null }): UserFavoriteFormGroup {
    const userFavoriteRawValue = {
      ...this.getFormDefaults(),
      ...userFavorite,
    };
    return new FormGroup<UserFavoriteFormGroupContent>({
      id: new FormControl(
        { value: userFavoriteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      product: new FormControl(userFavoriteRawValue.product),
    });
  }

  getUserFavorite(form: UserFavoriteFormGroup): IUserFavorite | NewUserFavorite {
    return form.getRawValue() as IUserFavorite | NewUserFavorite;
  }

  resetForm(form: UserFavoriteFormGroup, userFavorite: UserFavoriteFormGroupInput): void {
    const userFavoriteRawValue = { ...this.getFormDefaults(), ...userFavorite };
    form.reset(
      {
        ...userFavoriteRawValue,
        id: { value: userFavoriteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserFavoriteFormDefaults {
    return {
      id: null,
    };
  }
}
