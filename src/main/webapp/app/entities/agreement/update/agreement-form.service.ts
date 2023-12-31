import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAgreement, NewAgreement } from '../agreement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAgreement for edit and NewAgreementFormGroupInput for create.
 */
type AgreementFormGroupInput = IAgreement | PartialWithRequiredKeyOf<NewAgreement>;

type AgreementFormDefaults = Pick<NewAgreement, 'id'>;

type AgreementFormGroupContent = {
  id: FormControl<IAgreement['id'] | NewAgreement['id']>;
  name: FormControl<IAgreement['name']>;
  startDate: FormControl<IAgreement['startDate']>;
  endDate: FormControl<IAgreement['endDate']>;
  activationStatusClassId: FormControl<IAgreement['activationStatusClassId']>;
  infrastructureBenefit: FormControl<IAgreement['infrastructureBenefit']>;
  extraBenefit: FormControl<IAgreement['extraBenefit']>;
  provider: FormControl<IAgreement['provider']>;
  consumer: FormControl<IAgreement['consumer']>;
};

export type AgreementFormGroup = FormGroup<AgreementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AgreementFormService {
  createAgreementFormGroup(agreement: AgreementFormGroupInput = { id: null }): AgreementFormGroup {
    const agreementRawValue = {
      ...this.getFormDefaults(),
      ...agreement,
    };
    return new FormGroup<AgreementFormGroupContent>({
      id: new FormControl(
        { value: agreementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(agreementRawValue.name, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(agreementRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(agreementRawValue.endDate, {
        validators: [Validators.required],
      }),
      activationStatusClassId: new FormControl(agreementRawValue.activationStatusClassId, {
        validators: [Validators.required],
      }),
      infrastructureBenefit: new FormControl(agreementRawValue.infrastructureBenefit, {
        validators: [Validators.required],
      }),
      extraBenefit: new FormControl(agreementRawValue.extraBenefit),
      provider: new FormControl(agreementRawValue.provider),
      consumer: new FormControl(agreementRawValue.consumer),
    });
  }

  getAgreement(form: AgreementFormGroup): IAgreement | NewAgreement {
    return form.getRawValue() as IAgreement | NewAgreement;
  }

  resetForm(form: AgreementFormGroup, agreement: AgreementFormGroupInput): void {
    const agreementRawValue = { ...this.getFormDefaults(), ...agreement };
    form.reset(
      {
        ...agreementRawValue,
        id: { value: agreementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AgreementFormDefaults {
    return {
      id: null,
    };
  }
}
