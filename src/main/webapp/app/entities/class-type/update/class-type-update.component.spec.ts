import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClassTypeService } from '../service/class-type.service';
import { IClassType } from '../class-type.model';
import { ClassTypeFormService } from './class-type-form.service';

import { ClassTypeUpdateComponent } from './class-type-update.component';

describe('ClassType Management Update Component', () => {
  let comp: ClassTypeUpdateComponent;
  let fixture: ComponentFixture<ClassTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classTypeFormService: ClassTypeFormService;
  let classTypeService: ClassTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ClassTypeUpdateComponent],
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
      .overrideTemplate(ClassTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classTypeFormService = TestBed.inject(ClassTypeFormService);
    classTypeService = TestBed.inject(ClassTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const classType: IClassType = { id: 456 };

      activatedRoute.data = of({ classType });
      comp.ngOnInit();

      expect(comp.classType).toEqual(classType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassType>>();
      const classType = { id: 123 };
      jest.spyOn(classTypeFormService, 'getClassType').mockReturnValue(classType);
      jest.spyOn(classTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classType }));
      saveSubject.complete();

      // THEN
      expect(classTypeFormService.getClassType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(classTypeService.update).toHaveBeenCalledWith(expect.objectContaining(classType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassType>>();
      const classType = { id: 123 };
      jest.spyOn(classTypeFormService, 'getClassType').mockReturnValue({ id: null });
      jest.spyOn(classTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classType }));
      saveSubject.complete();

      // THEN
      expect(classTypeFormService.getClassType).toHaveBeenCalled();
      expect(classTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassType>>();
      const classType = { id: 123 };
      jest.spyOn(classTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
