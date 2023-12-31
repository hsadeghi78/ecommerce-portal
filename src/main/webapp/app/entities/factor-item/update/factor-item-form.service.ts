import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFactorItem, NewFactorItem } from '../factor-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFactorItem for edit and NewFactorItemFormGroupInput for create.
 */
type FactorItemFormGroupInput = IFactorItem | PartialWithRequiredKeyOf<NewFactorItem>;

type FactorItemFormDefaults = Pick<NewFactorItem, 'id'>;

type FactorItemFormGroupContent = {
  id: FormControl<IFactorItem['id'] | NewFactorItem['id']>;
  rowNum: FormControl<IFactorItem['rowNum']>;
  title: FormControl<IFactorItem['title']>;
  count: FormControl<IFactorItem['count']>;
  discount: FormControl<IFactorItem['discount']>;
  tax: FormControl<IFactorItem['tax']>;
  description: FormControl<IFactorItem['description']>;
  factor: FormControl<IFactorItem['factor']>;
  product: FormControl<IFactorItem['product']>;
};

export type FactorItemFormGroup = FormGroup<FactorItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FactorItemFormService {
  createFactorItemFormGroup(factorItem: FactorItemFormGroupInput = { id: null }): FactorItemFormGroup {
    const factorItemRawValue = {
      ...this.getFormDefaults(),
      ...factorItem,
    };
    return new FormGroup<FactorItemFormGroupContent>({
      id: new FormControl(
        { value: factorItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      rowNum: new FormControl(factorItemRawValue.rowNum, {
        validators: [Validators.required],
      }),
      title: new FormControl(factorItemRawValue.title, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      count: new FormControl(factorItemRawValue.count, {
        validators: [Validators.required],
      }),
      discount: new FormControl(factorItemRawValue.discount),
      tax: new FormControl(factorItemRawValue.tax),
      description: new FormControl(factorItemRawValue.description, {
        validators: [Validators.maxLength(300)],
      }),
      factor: new FormControl(factorItemRawValue.factor, {
        validators: [Validators.required],
      }),
      product: new FormControl(factorItemRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getFactorItem(form: FactorItemFormGroup): IFactorItem | NewFactorItem {
    return form.getRawValue() as IFactorItem | NewFactorItem;
  }

  resetForm(form: FactorItemFormGroup, factorItem: FactorItemFormGroupInput): void {
    const factorItemRawValue = { ...this.getFormDefaults(), ...factorItem };
    form.reset(
      {
        ...factorItemRawValue,
        id: { value: factorItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FactorItemFormDefaults {
    return {
      id: null,
    };
  }
}
