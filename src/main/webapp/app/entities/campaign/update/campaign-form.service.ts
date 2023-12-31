import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICampaign, NewCampaign } from '../campaign.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICampaign for edit and NewCampaignFormGroupInput for create.
 */
type CampaignFormGroupInput = ICampaign | PartialWithRequiredKeyOf<NewCampaign>;

type CampaignFormDefaults = Pick<NewCampaign, 'id' | 'products'>;

type CampaignFormGroupContent = {
  id: FormControl<ICampaign['id'] | NewCampaign['id']>;
  title: FormControl<ICampaign['title']>;
  startDate: FormControl<ICampaign['startDate']>;
  endDate: FormControl<ICampaign['endDate']>;
  photo: FormControl<ICampaign['photo']>;
  photoContentType: FormControl<ICampaign['photoContentType']>;
  description: FormControl<ICampaign['description']>;
  products: FormControl<ICampaign['products']>;
};

export type CampaignFormGroup = FormGroup<CampaignFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CampaignFormService {
  createCampaignFormGroup(campaign: CampaignFormGroupInput = { id: null }): CampaignFormGroup {
    const campaignRawValue = {
      ...this.getFormDefaults(),
      ...campaign,
    };
    return new FormGroup<CampaignFormGroupContent>({
      id: new FormControl(
        { value: campaignRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(campaignRawValue.title, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      startDate: new FormControl(campaignRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(campaignRawValue.endDate, {
        validators: [Validators.required],
      }),
      photo: new FormControl(campaignRawValue.photo, {
        validators: [Validators.required],
      }),
      photoContentType: new FormControl(campaignRawValue.photoContentType),
      description: new FormControl(campaignRawValue.description, {
        validators: [Validators.maxLength(1000)],
      }),
      products: new FormControl(campaignRawValue.products ?? []),
    });
  }

  getCampaign(form: CampaignFormGroup): ICampaign | NewCampaign {
    return form.getRawValue() as ICampaign | NewCampaign;
  }

  resetForm(form: CampaignFormGroup, campaign: CampaignFormGroupInput): void {
    const campaignRawValue = { ...this.getFormDefaults(), ...campaign };
    form.reset(
      {
        ...campaignRawValue,
        id: { value: campaignRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CampaignFormDefaults {
    return {
      id: null,
      products: [],
    };
  }
}
