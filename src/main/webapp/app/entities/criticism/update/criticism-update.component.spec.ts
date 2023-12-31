import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CriticismService } from '../service/criticism.service';
import { ICriticism } from '../criticism.model';
import { CriticismFormService } from './criticism-form.service';

import { CriticismUpdateComponent } from './criticism-update.component';

describe('Criticism Management Update Component', () => {
  let comp: CriticismUpdateComponent;
  let fixture: ComponentFixture<CriticismUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let criticismFormService: CriticismFormService;
  let criticismService: CriticismService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CriticismUpdateComponent],
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
      .overrideTemplate(CriticismUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CriticismUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    criticismFormService = TestBed.inject(CriticismFormService);
    criticismService = TestBed.inject(CriticismService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const criticism: ICriticism = { id: 456 };

      activatedRoute.data = of({ criticism });
      comp.ngOnInit();

      expect(comp.criticism).toEqual(criticism);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICriticism>>();
      const criticism = { id: 123 };
      jest.spyOn(criticismFormService, 'getCriticism').mockReturnValue(criticism);
      jest.spyOn(criticismService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ criticism });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: criticism }));
      saveSubject.complete();

      // THEN
      expect(criticismFormService.getCriticism).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(criticismService.update).toHaveBeenCalledWith(expect.objectContaining(criticism));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICriticism>>();
      const criticism = { id: 123 };
      jest.spyOn(criticismFormService, 'getCriticism').mockReturnValue({ id: null });
      jest.spyOn(criticismService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ criticism: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: criticism }));
      saveSubject.complete();

      // THEN
      expect(criticismFormService.getCriticism).toHaveBeenCalled();
      expect(criticismService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICriticism>>();
      const criticism = { id: 123 };
      jest.spyOn(criticismService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ criticism });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(criticismService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
