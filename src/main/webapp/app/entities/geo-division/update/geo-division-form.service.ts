import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGeoDivision, NewGeoDivision } from '../geo-division.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGeoDivision for edit and NewGeoDivisionFormGroupInput for create.
 */
type GeoDivisionFormGroupInput = IGeoDivision | PartialWithRequiredKeyOf<NewGeoDivision>;

type GeoDivisionFormDefaults = Pick<NewGeoDivision, 'id'>;

type GeoDivisionFormGroupContent = {
  id: FormControl<IGeoDivision['id'] | NewGeoDivision['id']>;
  name: FormControl<IGeoDivision['name']>;
  code: FormControl<IGeoDivision['code']>;
  level: FormControl<IGeoDivision['level']>;
  parent: FormControl<IGeoDivision['parent']>;
};

export type GeoDivisionFormGroup = FormGroup<GeoDivisionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GeoDivisionFormService {
  createGeoDivisionFormGroup(geoDivision: GeoDivisionFormGroupInput = { id: null }): GeoDivisionFormGroup {
    const geoDivisionRawValue = {
      ...this.getFormDefaults(),
      ...geoDivision,
    };
    return new FormGroup<GeoDivisionFormGroupContent>({
      id: new FormControl(
        { value: geoDivisionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(geoDivisionRawValue.name, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      code: new FormControl(geoDivisionRawValue.code, {
        validators: [Validators.required],
      }),
      level: new FormControl(geoDivisionRawValue.level, {
        validators: [Validators.required],
      }),
      parent: new FormControl(geoDivisionRawValue.parent),
    });
  }

  getGeoDivision(form: GeoDivisionFormGroup): IGeoDivision | NewGeoDivision {
    return form.getRawValue() as IGeoDivision | NewGeoDivision;
  }

  resetForm(form: GeoDivisionFormGroup, geoDivision: GeoDivisionFormGroupInput): void {
    const geoDivisionRawValue = { ...this.getFormDefaults(), ...geoDivision };
    form.reset(
      {
        ...geoDivisionRawValue,
        id: { value: geoDivisionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): GeoDivisionFormDefaults {
    return {
      id: null,
    };
  }
}
