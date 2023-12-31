import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IParty, NewParty } from '../party.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParty for edit and NewPartyFormGroupInput for create.
 */
type PartyFormGroupInput = IParty | PartialWithRequiredKeyOf<NewParty>;

type PartyFormDefaults = Pick<NewParty, 'id' | 'activationStatus' | 'personType'>;

type PartyFormGroupContent = {
  id: FormControl<IParty['id'] | NewParty['id']>;
  title: FormControl<IParty['title']>;
  partyCode: FormControl<IParty['partyCode']>;
  tradeTitle: FormControl<IParty['tradeTitle']>;
  activationDate: FormControl<IParty['activationDate']>;
  expirationDate: FormControl<IParty['expirationDate']>;
  activationStatus: FormControl<IParty['activationStatus']>;
  photo: FormControl<IParty['photo']>;
  photoContentType: FormControl<IParty['photoContentType']>;
  personType: FormControl<IParty['personType']>;
};

export type PartyFormGroup = FormGroup<PartyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PartyFormService {
  createPartyFormGroup(party: PartyFormGroupInput = { id: null }): PartyFormGroup {
    const partyRawValue = {
      ...this.getFormDefaults(),
      ...party,
    };
    return new FormGroup<PartyFormGroupContent>({
      id: new FormControl(
        { value: partyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(partyRawValue.title, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      partyCode: new FormControl(partyRawValue.partyCode, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      tradeTitle: new FormControl(partyRawValue.tradeTitle, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      activationDate: new FormControl(partyRawValue.activationDate, {
        validators: [Validators.required],
      }),
      expirationDate: new FormControl(partyRawValue.expirationDate),
      activationStatus: new FormControl(partyRawValue.activationStatus, {
        validators: [Validators.required],
      }),
      photo: new FormControl(partyRawValue.photo),
      photoContentType: new FormControl(partyRawValue.photoContentType),
      personType: new FormControl(partyRawValue.personType, {
        validators: [Validators.required],
      }),
    });
  }

  getParty(form: PartyFormGroup): IParty | NewParty {
    return form.getRawValue() as IParty | NewParty;
  }

  resetForm(form: PartyFormGroup, party: PartyFormGroupInput): void {
    const partyRawValue = { ...this.getFormDefaults(), ...party };
    form.reset(
      {
        ...partyRawValue,
        id: { value: partyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PartyFormDefaults {
    return {
      id: null,
      activationStatus: false,
      personType: false,
    };
  }
}
