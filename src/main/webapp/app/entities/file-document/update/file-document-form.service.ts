import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFileDocument, NewFileDocument } from '../file-document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFileDocument for edit and NewFileDocumentFormGroupInput for create.
 */
type FileDocumentFormGroupInput = IFileDocument | PartialWithRequiredKeyOf<NewFileDocument>;

type FileDocumentFormDefaults = Pick<NewFileDocument, 'id' | 'prices'>;

type FileDocumentFormGroupContent = {
  id: FormControl<IFileDocument['id'] | NewFileDocument['id']>;
  fileName: FormControl<IFileDocument['fileName']>;
  fileContent: FormControl<IFileDocument['fileContent']>;
  fileContentContentType: FormControl<IFileDocument['fileContentContentType']>;
  filePath: FormControl<IFileDocument['filePath']>;
  description: FormControl<IFileDocument['description']>;
  prices: FormControl<IFileDocument['prices']>;
};

export type FileDocumentFormGroup = FormGroup<FileDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FileDocumentFormService {
  createFileDocumentFormGroup(fileDocument: FileDocumentFormGroupInput = { id: null }): FileDocumentFormGroup {
    const fileDocumentRawValue = {
      ...this.getFormDefaults(),
      ...fileDocument,
    };
    return new FormGroup<FileDocumentFormGroupContent>({
      id: new FormControl(
        { value: fileDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fileName: new FormControl(fileDocumentRawValue.fileName, {
        validators: [Validators.required, Validators.maxLength(250)],
      }),
      fileContent: new FormControl(fileDocumentRawValue.fileContent, {
        validators: [Validators.required],
      }),
      fileContentContentType: new FormControl(fileDocumentRawValue.fileContentContentType),
      filePath: new FormControl(fileDocumentRawValue.filePath, {
        validators: [Validators.maxLength(2000)],
      }),
      description: new FormControl(fileDocumentRawValue.description, {
        validators: [Validators.required, Validators.maxLength(3000)],
      }),
      prices: new FormControl(fileDocumentRawValue.prices ?? []),
    });
  }

  getFileDocument(form: FileDocumentFormGroup): IFileDocument | NewFileDocument {
    return form.getRawValue() as IFileDocument | NewFileDocument;
  }

  resetForm(form: FileDocumentFormGroup, fileDocument: FileDocumentFormGroupInput): void {
    const fileDocumentRawValue = { ...this.getFormDefaults(), ...fileDocument };
    form.reset(
      {
        ...fileDocumentRawValue,
        id: { value: fileDocumentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FileDocumentFormDefaults {
    return {
      id: null,
      prices: [],
    };
  }
}
