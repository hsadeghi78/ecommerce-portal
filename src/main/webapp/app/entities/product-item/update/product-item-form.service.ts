import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductItem, NewProductItem } from '../product-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductItem for edit and NewProductItemFormGroupInput for create.
 */
type ProductItemFormGroupInput = IProductItem | PartialWithRequiredKeyOf<NewProductItem>;

type ProductItemFormDefaults = Pick<NewProductItem, 'id'>;

type ProductItemFormGroupContent = {
  id: FormControl<IProductItem['id'] | NewProductItem['id']>;
  typeClassId: FormControl<IProductItem['typeClassId']>;
  name: FormControl<IProductItem['name']>;
  value: FormControl<IProductItem['value']>;
  product: FormControl<IProductItem['product']>;
};

export type ProductItemFormGroup = FormGroup<ProductItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductItemFormService {
  createProductItemFormGroup(productItem: ProductItemFormGroupInput = { id: null }): ProductItemFormGroup {
    const productItemRawValue = {
      ...this.getFormDefaults(),
      ...productItem,
    };
    return new FormGroup<ProductItemFormGroupContent>({
      id: new FormControl(
        { value: productItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      typeClassId: new FormControl(productItemRawValue.typeClassId, {
        validators: [Validators.required],
      }),
      name: new FormControl(productItemRawValue.name, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      value: new FormControl(productItemRawValue.value, {
        validators: [Validators.required, Validators.maxLength(800)],
      }),
      product: new FormControl(productItemRawValue.product, {
        validators: [Validators.required],
      }),
    });
  }

  getProductItem(form: ProductItemFormGroup): IProductItem | NewProductItem {
    return form.getRawValue() as IProductItem | NewProductItem;
  }

  resetForm(form: ProductItemFormGroup, productItem: ProductItemFormGroupInput): void {
    const productItemRawValue = { ...this.getFormDefaults(), ...productItem };
    form.reset(
      {
        ...productItemRawValue,
        id: { value: productItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductItemFormDefaults {
    return {
      id: null,
    };
  }
}
