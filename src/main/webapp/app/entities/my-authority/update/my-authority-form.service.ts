import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMyAuthority, NewMyAuthority } from '../my-authority.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMyAuthority for edit and NewMyAuthorityFormGroupInput for create.
 */
type MyAuthorityFormGroupInput = IMyAuthority | PartialWithRequiredKeyOf<NewMyAuthority>;

type MyAuthorityFormDefaults = Pick<NewMyAuthority, 'id'>;

type MyAuthorityFormGroupContent = {
  id: FormControl<IMyAuthority['id'] | NewMyAuthority['id']>;
  name: FormControl<IMyAuthority['name']>;
  displayName: FormControl<IMyAuthority['displayName']>;
  parent: FormControl<IMyAuthority['parent']>;
};

export type MyAuthorityFormGroup = FormGroup<MyAuthorityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MyAuthorityFormService {
  createMyAuthorityFormGroup(myAuthority: MyAuthorityFormGroupInput = { id: null }): MyAuthorityFormGroup {
    const myAuthorityRawValue = {
      ...this.getFormDefaults(),
      ...myAuthority,
    };
    return new FormGroup<MyAuthorityFormGroupContent>({
      id: new FormControl(
        { value: myAuthorityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(myAuthorityRawValue.name, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      displayName: new FormControl(myAuthorityRawValue.displayName, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      parent: new FormControl(myAuthorityRawValue.parent),
    });
  }

  getMyAuthority(form: MyAuthorityFormGroup): IMyAuthority | NewMyAuthority {
    return form.getRawValue() as IMyAuthority | NewMyAuthority;
  }

  resetForm(form: MyAuthorityFormGroup, myAuthority: MyAuthorityFormGroupInput): void {
    const myAuthorityRawValue = { ...this.getFormDefaults(), ...myAuthority };
    form.reset(
      {
        ...myAuthorityRawValue,
        id: { value: myAuthorityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MyAuthorityFormDefaults {
    return {
      id: null,
    };
  }
}
