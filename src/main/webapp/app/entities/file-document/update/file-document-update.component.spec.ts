import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FileDocumentService } from '../service/file-document.service';
import { IFileDocument } from '../file-document.model';
import { FileDocumentFormService } from './file-document-form.service';

import { FileDocumentUpdateComponent } from './file-document-update.component';

describe('FileDocument Management Update Component', () => {
  let comp: FileDocumentUpdateComponent;
  let fixture: ComponentFixture<FileDocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fileDocumentFormService: FileDocumentFormService;
  let fileDocumentService: FileDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FileDocumentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FileDocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FileDocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fileDocumentFormService = TestBed.inject(FileDocumentFormService);
    fileDocumentService = TestBed.inject(FileDocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fileDocument: IFileDocument = { id: 456 };

      activatedRoute.data = of({ fileDocument });
      comp.ngOnInit();

      expect(comp.fileDocument).toEqual(fileDocument);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFileDocument>>();
      const fileDocument = { id: 123 };
      jest.spyOn(fileDocumentFormService, 'getFileDocument').mockReturnValue(fileDocument);
      jest.spyOn(fileDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileDocument }));
      saveSubject.complete();

      // THEN
      expect(fileDocumentFormService.getFileDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fileDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(fileDocument));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFileDocument>>();
      const fileDocument = { id: 123 };
      jest.spyOn(fileDocumentFormService, 'getFileDocument').mockReturnValue({ id: null });
      jest.spyOn(fileDocumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fileDocument }));
      saveSubject.complete();

      // THEN
      expect(fileDocumentFormService.getFileDocument).toHaveBeenCalled();
      expect(fileDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFileDocument>>();
      const fileDocument = { id: 123 };
      jest.spyOn(fileDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fileDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fileDocumentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
