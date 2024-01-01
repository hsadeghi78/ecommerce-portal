import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id' | 'used' | 'documents' | 'campaigns'>;

type ProductFormGroupContent = {
  id: FormControl<IProduct['id'] | NewProduct['id']>;
  name: FormControl<IProduct['name']>;
  typeClassId: FormControl<IProduct['typeClassId']>;
  brandClassId: FormControl<IProduct['brandClassId']>;
  sizee: FormControl<IProduct['sizee']>;
  regularSizeClassId: FormControl<IProduct['regularSizeClassId']>;
  languageClassId: FormControl<IProduct['languageClassId']>;
  description: FormControl<IProduct['description']>;
  keywords: FormControl<IProduct['keywords']>;
  photo1: FormControl<IProduct['photo1']>;
  photo1ContentType: FormControl<IProduct['photo1ContentType']>;
  nationalityClassId: FormControl<IProduct['nationalityClassId']>;
  count: FormControl<IProduct['count']>;
  discount: FormControl<IProduct['discount']>;
  originalPrice: FormControl<IProduct['originalPrice']>;
  finalPrice: FormControl<IProduct['finalPrice']>;
  publishDate: FormControl<IProduct['publishDate']>;
  transportDate: FormControl<IProduct['transportDate']>;
  currencyClassId: FormControl<IProduct['currencyClassId']>;
  bonus: FormControl<IProduct['bonus']>;
  warrantyClassId: FormControl<IProduct['warrantyClassId']>;
  deliveryPlaceClassId: FormControl<IProduct['deliveryPlaceClassId']>;
  paymentPlaceClassId: FormControl<IProduct['paymentPlaceClassId']>;
  performance: FormControl<IProduct['performance']>;
  originalityClassId: FormControl<IProduct['originalityClassId']>;
  satisfaction: FormControl<IProduct['satisfaction']>;
  used: FormControl<IProduct['used']>;
  documents: FormControl<IProduct['documents']>;
  category: FormControl<IProduct['category']>;
  party: FormControl<IProduct['party']>;
  parent: FormControl<IProduct['parent']>;
  campaigns: FormControl<IProduct['campaigns']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = {
      ...this.getFormDefaults(),
      ...product,
    };
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(productRawValue.name, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      typeClassId: new FormControl(productRawValue.typeClassId),
      brandClassId: new FormControl(productRawValue.brandClassId),
      sizee: new FormControl(productRawValue.sizee, {
        validators: [Validators.maxLength(100)],
      }),
      regularSizeClassId: new FormControl(productRawValue.regularSizeClassId, {
        validators: [Validators.required],
      }),
      languageClassId: new FormControl(productRawValue.languageClassId, {
        validators: [Validators.required],
      }),
      description: new FormControl(productRawValue.description, {
        validators: [Validators.maxLength(2500)],
      }),
      keywords: new FormControl(productRawValue.keywords, {
        validators: [Validators.maxLength(500)],
      }),
      photo1: new FormControl(productRawValue.photo1, {
        validators: [Validators.required],
      }),
      photo1ContentType: new FormControl(productRawValue.photo1ContentType),
      nationalityClassId: new FormControl(productRawValue.nationalityClassId),
      count: new FormControl(productRawValue.count, {
        validators: [Validators.required],
      }),
      discount: new FormControl(productRawValue.discount),
      originalPrice: new FormControl(productRawValue.originalPrice, {
        validators: [Validators.required],
      }),
      finalPrice: new FormControl(productRawValue.finalPrice, {
        validators: [Validators.required],
      }),
      publishDate: new FormControl(productRawValue.publishDate, {
        validators: [Validators.required],
      }),
      transportDate: new FormControl(productRawValue.transportDate, {
        validators: [Validators.required],
      }),
      currencyClassId: new FormControl(productRawValue.currencyClassId, {
        validators: [Validators.required],
      }),
      bonus: new FormControl(productRawValue.bonus),
      warrantyClassId: new FormControl(productRawValue.warrantyClassId),
      deliveryPlaceClassId: new FormControl(productRawValue.deliveryPlaceClassId),
      paymentPlaceClassId: new FormControl(productRawValue.paymentPlaceClassId),
      performance: new FormControl(productRawValue.performance),
      originalityClassId: new FormControl(productRawValue.originalityClassId),
      satisfaction: new FormControl(productRawValue.satisfaction),
      used: new FormControl(productRawValue.used, {
        validators: [Validators.required],
      }),
      documents: new FormControl(productRawValue.documents ?? []),
      category: new FormControl(productRawValue.category, {
        validators: [Validators.required],
      }),
      party: new FormControl(productRawValue.party),
      parent: new FormControl(productRawValue.parent),
      campaigns: new FormControl(productRawValue.campaigns ?? []),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return form.getRawValue() as IProduct | NewProduct;
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = { ...this.getFormDefaults(), ...product };
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    return {
      id: null,
      used: false,
      documents: [],
      campaigns: [],
    };
  }
}
