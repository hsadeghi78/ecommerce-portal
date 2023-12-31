import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILocation, NewLocation } from '../location.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILocation for edit and NewLocationFormGroupInput for create.
 */
type LocationFormGroupInput = ILocation | PartialWithRequiredKeyOf<NewLocation>;

type LocationFormDefaults = Pick<NewLocation, 'id'>;

type LocationFormGroupContent = {
  id: FormControl<ILocation['id'] | NewLocation['id']>;
  typeClassId: FormControl<ILocation['typeClassId']>;
  title: FormControl<ILocation['title']>;
  lat: FormControl<ILocation['lat']>;
  lon: FormControl<ILocation['lon']>;
  street1: FormControl<ILocation['street1']>;
  street2: FormControl<ILocation['street2']>;
  street3: FormControl<ILocation['street3']>;
  buildingNo: FormControl<ILocation['buildingNo']>;
  buildingName: FormControl<ILocation['buildingName']>;
  floor: FormControl<ILocation['floor']>;
  unit: FormControl<ILocation['unit']>;
  postalCode: FormControl<ILocation['postalCode']>;
  other: FormControl<ILocation['other']>;
  geoDivision: FormControl<ILocation['geoDivision']>;
  party: FormControl<ILocation['party']>;
};

export type LocationFormGroup = FormGroup<LocationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LocationFormService {
  createLocationFormGroup(location: LocationFormGroupInput = { id: null }): LocationFormGroup {
    const locationRawValue = {
      ...this.getFormDefaults(),
      ...location,
    };
    return new FormGroup<LocationFormGroupContent>({
      id: new FormControl(
        { value: locationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      typeClassId: new FormControl(locationRawValue.typeClassId, {
        validators: [Validators.required],
      }),
      title: new FormControl(locationRawValue.title, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      lat: new FormControl(locationRawValue.lat, {
        validators: [Validators.required],
      }),
      lon: new FormControl(locationRawValue.lon, {
        validators: [Validators.required],
      }),
      street1: new FormControl(locationRawValue.street1, {
        validators: [Validators.maxLength(200)],
      }),
      street2: new FormControl(locationRawValue.street2, {
        validators: [Validators.maxLength(200)],
      }),
      street3: new FormControl(locationRawValue.street3, {
        validators: [Validators.maxLength(200)],
      }),
      buildingNo: new FormControl(locationRawValue.buildingNo, {
        validators: [Validators.required],
      }),
      buildingName: new FormControl(locationRawValue.buildingName, {
        validators: [Validators.maxLength(100)],
      }),
      floor: new FormControl(locationRawValue.floor),
      unit: new FormControl(locationRawValue.unit, {
        validators: [Validators.required],
      }),
      postalCode: new FormControl(locationRawValue.postalCode, {
        validators: [Validators.required, Validators.maxLength(12)],
      }),
      other: new FormControl(locationRawValue.other),
      geoDivision: new FormControl(locationRawValue.geoDivision, {
        validators: [Validators.required],
      }),
      party: new FormControl(locationRawValue.party),
    });
  }

  getLocation(form: LocationFormGroup): ILocation | NewLocation {
    return form.getRawValue() as ILocation | NewLocation;
  }

  resetForm(form: LocationFormGroup, location: LocationFormGroupInput): void {
    const locationRawValue = { ...this.getFormDefaults(), ...location };
    form.reset(
      {
        ...locationRawValue,
        id: { value: locationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LocationFormDefaults {
    return {
      id: null,
    };
  }
}
