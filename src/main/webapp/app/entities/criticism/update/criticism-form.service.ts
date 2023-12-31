import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICriticism, NewCriticism } from '../criticism.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICriticism for edit and NewCriticismFormGroupInput for create.
 */
type CriticismFormGroupInput = ICriticism | PartialWithRequiredKeyOf<NewCriticism>;

type CriticismFormDefaults = Pick<NewCriticism, 'id'>;

type CriticismFormGroupContent = {
  id: FormControl<ICriticism['id'] | NewCriticism['id']>;
  fullName: FormControl<ICriticism['fullName']>;
  email: FormControl<ICriticism['email']>;
  contactNumber: FormControl<ICriticism['contactNumber']>;
  description: FormControl<ICriticism['description']>;
};

export type CriticismFormGroup = FormGroup<CriticismFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CriticismFormService {
  createCriticismFormGroup(criticism: CriticismFormGroupInput = { id: null }): CriticismFormGroup {
    const criticismRawValue = {
      ...this.getFormDefaults(),
      ...criticism,
    };
    return new FormGroup<CriticismFormGroupContent>({
      id: new FormControl(
        { value: criticismRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fullName: new FormControl(criticismRawValue.fullName, {
        validators: [Validators.required, Validators.maxLength(150)],
      }),
      email: new FormControl(criticismRawValue.email, {
        validators: [Validators.maxLength(150)],
      }),
      contactNumber: new FormControl(criticismRawValue.contactNumber, {
        validators: [Validators.maxLength(15)],
      }),
      description: new FormControl(criticismRawValue.description, {
        validators: [Validators.required, Validators.maxLength(3000)],
      }),
    });
  }

  getCriticism(form: CriticismFormGroup): ICriticism | NewCriticism {
    return form.getRawValue() as ICriticism | NewCriticism;
  }

  resetForm(form: CriticismFormGroup, criticism: CriticismFormGroupInput): void {
    const criticismRawValue = { ...this.getFormDefaults(), ...criticism };
    form.reset(
      {
        ...criticismRawValue,
        id: { value: criticismRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CriticismFormDefaults {
    return {
      id: null,
    };
  }
}
