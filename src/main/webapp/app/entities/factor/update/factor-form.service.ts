import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFactor, NewFactor } from '../factor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFactor for edit and NewFactorFormGroupInput for create.
 */
type FactorFormGroupInput = IFactor | PartialWithRequiredKeyOf<NewFactor>;

type FactorFormDefaults = Pick<NewFactor, 'id'>;

type FactorFormGroupContent = {
  id: FormControl<IFactor['id'] | NewFactor['id']>;
  title: FormControl<IFactor['title']>;
  factorCode: FormControl<IFactor['factorCode']>;
  lastStatusClassId: FormControl<IFactor['lastStatusClassId']>;
  paymentStateClassId: FormControl<IFactor['paymentStateClassId']>;
  categoryClassId: FormControl<IFactor['categoryClassId']>;
  totalPrice: FormControl<IFactor['totalPrice']>;
  discount: FormControl<IFactor['discount']>;
  discountCode: FormControl<IFactor['discountCode']>;
  finalTax: FormControl<IFactor['finalTax']>;
  payable: FormControl<IFactor['payable']>;
  description: FormControl<IFactor['description']>;
  location: FormControl<IFactor['location']>;
  buyerParty: FormControl<IFactor['buyerParty']>;
  sellerParty: FormControl<IFactor['sellerParty']>;
};

export type FactorFormGroup = FormGroup<FactorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FactorFormService {
  createFactorFormGroup(factor: FactorFormGroupInput = { id: null }): FactorFormGroup {
    const factorRawValue = {
      ...this.getFormDefaults(),
      ...factor,
    };
    return new FormGroup<FactorFormGroupContent>({
      id: new FormControl(
        { value: factorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(factorRawValue.title, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      factorCode: new FormControl(factorRawValue.factorCode, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      lastStatusClassId: new FormControl(factorRawValue.lastStatusClassId, {
        validators: [Validators.required],
      }),
      paymentStateClassId: new FormControl(factorRawValue.paymentStateClassId, {
        validators: [Validators.required],
      }),
      categoryClassId: new FormControl(factorRawValue.categoryClassId),
      totalPrice: new FormControl(factorRawValue.totalPrice, {
        validators: [Validators.required],
      }),
      discount: new FormControl(factorRawValue.discount),
      discountCode: new FormControl(factorRawValue.discountCode),
      finalTax: new FormControl(factorRawValue.finalTax),
      payable: new FormControl(factorRawValue.payable, {
        validators: [Validators.required],
      }),
      description: new FormControl(factorRawValue.description, {
        validators: [Validators.maxLength(1000)],
      }),
      location: new FormControl(factorRawValue.location, {
        validators: [Validators.required],
      }),
      buyerParty: new FormControl(factorRawValue.buyerParty),
      sellerParty: new FormControl(factorRawValue.sellerParty),
    });
  }

  getFactor(form: FactorFormGroup): IFactor | NewFactor {
    return form.getRawValue() as IFactor | NewFactor;
  }

  resetForm(form: FactorFormGroup, factor: FactorFormGroupInput): void {
    const factorRawValue = { ...this.getFormDefaults(), ...factor };
    form.reset(
      {
        ...factorRawValue,
        id: { value: factorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FactorFormDefaults {
    return {
      id: null,
    };
  }
}
