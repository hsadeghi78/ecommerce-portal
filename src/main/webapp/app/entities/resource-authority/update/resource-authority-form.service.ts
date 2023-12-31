import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IResourceAuthority, NewResourceAuthority } from '../resource-authority.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResourceAuthority for edit and NewResourceAuthorityFormGroupInput for create.
 */
type ResourceAuthorityFormGroupInput = IResourceAuthority | PartialWithRequiredKeyOf<NewResourceAuthority>;

type ResourceAuthorityFormDefaults = Pick<NewResourceAuthority, 'id'>;

type ResourceAuthorityFormGroupContent = {
  id: FormControl<IResourceAuthority['id'] | NewResourceAuthority['id']>;
  verb: FormControl<IResourceAuthority['verb']>;
  resource: FormControl<IResourceAuthority['resource']>;
  myAuthority: FormControl<IResourceAuthority['myAuthority']>;
};

export type ResourceAuthorityFormGroup = FormGroup<ResourceAuthorityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResourceAuthorityFormService {
  createResourceAuthorityFormGroup(resourceAuthority: ResourceAuthorityFormGroupInput = { id: null }): ResourceAuthorityFormGroup {
    const resourceAuthorityRawValue = {
      ...this.getFormDefaults(),
      ...resourceAuthority,
    };
    return new FormGroup<ResourceAuthorityFormGroupContent>({
      id: new FormControl(
        { value: resourceAuthorityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      verb: new FormControl(resourceAuthorityRawValue.verb, {
        validators: [Validators.required],
      }),
      resource: new FormControl(resourceAuthorityRawValue.resource),
      myAuthority: new FormControl(resourceAuthorityRawValue.myAuthority),
    });
  }

  getResourceAuthority(form: ResourceAuthorityFormGroup): IResourceAuthority | NewResourceAuthority {
    return form.getRawValue() as IResourceAuthority | NewResourceAuthority;
  }

  resetForm(form: ResourceAuthorityFormGroup, resourceAuthority: ResourceAuthorityFormGroupInput): void {
    const resourceAuthorityRawValue = { ...this.getFormDefaults(), ...resourceAuthority };
    form.reset(
      {
        ...resourceAuthorityRawValue,
        id: { value: resourceAuthorityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ResourceAuthorityFormDefaults {
    return {
      id: null,
    };
  }
}
