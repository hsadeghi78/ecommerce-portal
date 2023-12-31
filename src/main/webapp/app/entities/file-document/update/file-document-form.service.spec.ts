import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../file-document.test-samples';

import { FileDocumentFormService } from './file-document-form.service';

describe('FileDocument Form Service', () => {
  let service: FileDocumentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FileDocumentFormService);
  });

  describe('Service methods', () => {
    describe('createFileDocumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFileDocumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fileName: expect.any(Object),
            fileContent: expect.any(Object),
            filePath: expect.any(Object),
            description: expect.any(Object),
            prices: expect.any(Object),
          }),
        );
      });

      it('passing IFileDocument should create a new form with FormGroup', () => {
        const formGroup = service.createFileDocumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fileName: expect.any(Object),
            fileContent: expect.any(Object),
            filePath: expect.any(Object),
            description: expect.any(Object),
            prices: expect.any(Object),
          }),
        );
      });
    });

    describe('getFileDocument', () => {
      it('should return NewFileDocument for default FileDocument initial value', () => {
        const formGroup = service.createFileDocumentFormGroup(sampleWithNewData);

        const fileDocument = service.getFileDocument(formGroup) as any;

        expect(fileDocument).toMatchObject(sampleWithNewData);
      });

      it('should return NewFileDocument for empty FileDocument initial value', () => {
        const formGroup = service.createFileDocumentFormGroup();

        const fileDocument = service.getFileDocument(formGroup) as any;

        expect(fileDocument).toMatchObject({});
      });

      it('should return IFileDocument', () => {
        const formGroup = service.createFileDocumentFormGroup(sampleWithRequiredData);

        const fileDocument = service.getFileDocument(formGroup) as any;

        expect(fileDocument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFileDocument should not enable id FormControl', () => {
        const formGroup = service.createFileDocumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFileDocument should disable id FormControl', () => {
        const formGroup = service.createFileDocumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
