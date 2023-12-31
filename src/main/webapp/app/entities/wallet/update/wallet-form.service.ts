import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWallet, NewWallet } from '../wallet.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWallet for edit and NewWalletFormGroupInput for create.
 */
type WalletFormGroupInput = IWallet | PartialWithRequiredKeyOf<NewWallet>;

type WalletFormDefaults = Pick<NewWallet, 'id'>;

type WalletFormGroupContent = {
  id: FormControl<IWallet['id'] | NewWallet['id']>;
  transTypeClassId: FormControl<IWallet['transTypeClassId']>;
  stock: FormControl<IWallet['stock']>;
  description: FormControl<IWallet['description']>;
  deposit: FormControl<IWallet['deposit']>;
  withdrawal: FormControl<IWallet['withdrawal']>;
};

export type WalletFormGroup = FormGroup<WalletFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WalletFormService {
  createWalletFormGroup(wallet: WalletFormGroupInput = { id: null }): WalletFormGroup {
    const walletRawValue = {
      ...this.getFormDefaults(),
      ...wallet,
    };
    return new FormGroup<WalletFormGroupContent>({
      id: new FormControl(
        { value: walletRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      transTypeClassId: new FormControl(walletRawValue.transTypeClassId),
      stock: new FormControl(walletRawValue.stock, {
        validators: [Validators.required],
      }),
      description: new FormControl(walletRawValue.description, {
        validators: [Validators.maxLength(1000)],
      }),
      deposit: new FormControl(walletRawValue.deposit),
      withdrawal: new FormControl(walletRawValue.withdrawal),
    });
  }

  getWallet(form: WalletFormGroup): IWallet | NewWallet {
    return form.getRawValue() as IWallet | NewWallet;
  }

  resetForm(form: WalletFormGroup, wallet: WalletFormGroupInput): void {
    const walletRawValue = { ...this.getFormDefaults(), ...wallet };
    form.reset(
      {
        ...walletRawValue,
        id: { value: walletRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WalletFormDefaults {
    return {
      id: null,
    };
  }
}
