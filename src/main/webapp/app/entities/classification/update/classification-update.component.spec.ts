import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IClassType } from 'app/entities/class-type/class-type.model';
import { ClassTypeService } from 'app/entities/class-type/service/class-type.service';
import { ClassificationService } from '../service/classification.service';
import { IClassification } from '../classification.model';
import { ClassificationFormService } from './classification-form.service';

import { ClassificationUpdateComponent } from './classification-update.component';

describe('Classification Management Update Component', () => {
  let comp: ClassificationUpdateComponent;
  let fixture: ComponentFixture<ClassificationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classificationFormService: ClassificationFormService;
  let classificationService: ClassificationService;
  let classTypeService: ClassTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ClassificationUpdateComponent],
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
      .overrideTemplate(ClassificationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassificationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classificationFormService = TestBed.inject(ClassificationFormService);
    classificationService = TestBed.inject(ClassificationService);
    classTypeService = TestBed.inject(ClassTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ClassType query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const classType: IClassType = { id: 500 };
      classification.classType = classType;

      const classTypeCollection: IClassType[] = [{ id: 12967 }];
      jest.spyOn(classTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: classTypeCollection })));
      const additionalClassTypes = [classType];
      const expectedCollection: IClassType[] = [...additionalClassTypes, ...classTypeCollection];
      jest.spyOn(classTypeService, 'addClassTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(classTypeService.query).toHaveBeenCalled();
      expect(classTypeService.addClassTypeToCollectionIfMissing).toHaveBeenCalledWith(
        classTypeCollection,
        ...additionalClassTypes.map(expect.objectContaining),
      );
      expect(comp.classTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const classification: IClassification = { id: 456 };
      const classType: IClassType = { id: 3310 };
      classification.classType = classType;

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(comp.classTypesSharedCollection).toContain(classType);
      expect(comp.classification).toEqual(classification);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassification>>();
      const classification = { id: 123 };
      jest.spyOn(classificationFormService, 'getClassification').mockReturnValue(classification);
      jest.spyOn(classificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classification }));
      saveSubject.complete();

      // THEN
      expect(classificationFormService.getClassification).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(classificationService.update).toHaveBeenCalledWith(expect.objectContaining(classification));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassification>>();
      const classification = { id: 123 };
      jest.spyOn(classificationFormService, 'getClassification').mockReturnValue({ id: null });
      jest.spyOn(classificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classification: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classification }));
      saveSubject.complete();

      // THEN
      expect(classificationFormService.getClassification).toHaveBeenCalled();
      expect(classificationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassification>>();
      const classification = { id: 123 };
      jest.spyOn(classificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classificationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareClassType', () => {
      it('Should forward to classTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(classTypeService, 'compareClassType');
        comp.compareClassType(entity, entity2);
        expect(classTypeService.compareClassType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
