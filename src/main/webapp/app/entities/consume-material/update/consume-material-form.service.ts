import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConsumeMaterial, NewConsumeMaterial } from '../consume-material.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConsumeMaterial for edit and NewConsumeMaterialFormGroupInput for create.
 */
type ConsumeMaterialFormGroupInput = IConsumeMaterial | PartialWithRequiredKeyOf<NewConsumeMaterial>;

type ConsumeMaterialFormDefaults = Pick<NewConsumeMaterial, 'id'>;

type ConsumeMaterialFormGroupContent = {
  id: FormControl<IConsumeMaterial['id'] | NewConsumeMaterial['id']>;
  typeClassId: FormControl<IConsumeMaterial['typeClassId']>;
  name: FormControl<IConsumeMaterial['name']>;
  value: FormControl<IConsumeMaterial['value']>;
  product: FormControl<IConsumeMaterial['product']>;
};

export type ConsumeMaterialFormGroup = FormGroup<ConsumeMaterialFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConsumeMaterialFormService {
  createConsumeMaterialFormGroup(consumeMaterial: ConsumeMaterialFormGroupInput = { id: null }): ConsumeMaterialFormGroup {
    const consumeMaterialRawValue = {
      ...this.getFormDefaults(),
      ...consumeMaterial,
    };
    return new FormGroup<ConsumeMaterialFormGroupContent>({
      id: new FormControl(
        { value: consumeMaterialRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      typeClassId: new FormControl(consumeMaterialRawValue.typeClassId, {
        validators: [Validators.required],
      }),
      name: new FormControl(consumeMaterialRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      value: new FormControl(consumeMaterialRawValue.value, {
        validators: [Validators.required, Validators.maxLength(800)],
      }),
      product: new FormControl(consumeMaterialRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getConsumeMaterial(form: ConsumeMaterialFormGroup): IConsumeMaterial | NewConsumeMaterial {
    return form.getRawValue() as IConsumeMaterial | NewConsumeMaterial;
  }

  resetForm(form: ConsumeMaterialFormGroup, consumeMaterial: ConsumeMaterialFormGroupInput): void {
    const consumeMaterialRawValue = { ...this.getFormDefaults(), ...consumeMaterial };
    form.reset(
      {
        ...consumeMaterialRawValue,
        id: { value: consumeMaterialRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConsumeMaterialFormDefaults {
    return {
      id: null,
    };
  }
}
