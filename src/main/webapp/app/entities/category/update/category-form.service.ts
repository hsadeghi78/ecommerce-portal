import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICategory, NewCategory } from '../category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategory for edit and NewCategoryFormGroupInput for create.
 */
type CategoryFormGroupInput = ICategory | PartialWithRequiredKeyOf<NewCategory>;

type CategoryFormDefaults = Pick<NewCategory, 'id' | 'hasChild'>;

type CategoryFormGroupContent = {
  id: FormControl<ICategory['id'] | NewCategory['id']>;
  title: FormControl<ICategory['title']>;
  code: FormControl<ICategory['code']>;
  hasChild: FormControl<ICategory['hasChild']>;
  level: FormControl<ICategory['level']>;
  keywords: FormControl<ICategory['keywords']>;
  description: FormControl<ICategory['description']>;
  parent: FormControl<ICategory['parent']>;
};

export type CategoryFormGroup = FormGroup<CategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategoryFormService {
  createCategoryFormGroup(category: CategoryFormGroupInput = { id: null }): CategoryFormGroup {
    const categoryRawValue = {
      ...this.getFormDefaults(),
      ...category,
    };
    return new FormGroup<CategoryFormGroupContent>({
      id: new FormControl(
        { value: categoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(categoryRawValue.title, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      code: new FormControl(categoryRawValue.code, {
        validators: [Validators.required, Validators.maxLength(10)],
      }),
      hasChild: new FormControl(categoryRawValue.hasChild, {
        validators: [Validators.required],
      }),
      level: new FormControl(categoryRawValue.level, {
        validators: [Validators.required],
      }),
      keywords: new FormControl(categoryRawValue.keywords, {
        validators: [Validators.maxLength(500)],
      }),
      description: new FormControl(categoryRawValue.description, {
        validators: [Validators.maxLength(500)],
      }),
      parent: new FormControl(categoryRawValue.parent),
    });
  }

  getCategory(form: CategoryFormGroup): ICategory | NewCategory {
    return form.getRawValue() as ICategory | NewCategory;
  }

  resetForm(form: CategoryFormGroup, category: CategoryFormGroupInput): void {
    const categoryRawValue = { ...this.getFormDefaults(), ...category };
    form.reset(
      {
        ...categoryRawValue,
        id: { value: categoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CategoryFormDefaults {
    return {
      id: null,
      hasChild: false,
    };
  }
}
