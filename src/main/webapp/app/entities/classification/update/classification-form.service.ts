import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IClassification, NewClassification } from '../classification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClassification for edit and NewClassificationFormGroupInput for create.
 */
type ClassificationFormGroupInput = IClassification | PartialWithRequiredKeyOf<NewClassification>;

type ClassificationFormDefaults = Pick<NewClassification, 'id'>;

type ClassificationFormGroupContent = {
  id: FormControl<IClassification['id'] | NewClassification['id']>;
  title: FormControl<IClassification['title']>;
  classCode: FormControl<IClassification['classCode']>;
  description: FormControl<IClassification['description']>;
  languageClassId: FormControl<IClassification['languageClassId']>;
  classType: FormControl<IClassification['classType']>;
};

export type ClassificationFormGroup = FormGroup<ClassificationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClassificationFormService {
  createClassificationFormGroup(classification: ClassificationFormGroupInput = { id: null }): ClassificationFormGroup {
    const classificationRawValue = {
      ...this.getFormDefaults(),
      ...classification,
    };
    return new FormGroup<ClassificationFormGroupContent>({
      id: new FormControl(
        { value: classificationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(classificationRawValue.title, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      classCode: new FormControl(classificationRawValue.classCode, {
        validators: [Validators.required, Validators.maxLength(25)],
      }),
      description: new FormControl(classificationRawValue.description, {
        validators: [Validators.maxLength(300)],
      }),
      languageClassId: new FormControl(classificationRawValue.languageClassId, {
        validators: [Validators.required],
      }),
      classType: new FormControl(classificationRawValue.classType, {
        validators: [Validators.required],
      }),
    });
  }

  getClassification(form: ClassificationFormGroup): IClassification | NewClassification {
    return form.getRawValue() as IClassification | NewClassification;
  }

  resetForm(form: ClassificationFormGroup, classification: ClassificationFormGroupInput): void {
    const classificationRawValue = { ...this.getFormDefaults(), ...classification };
    form.reset(
      {
        ...classificationRawValue,
        id: { value: classificationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClassificationFormDefaults {
    return {
      id: null,
    };
  }
}
