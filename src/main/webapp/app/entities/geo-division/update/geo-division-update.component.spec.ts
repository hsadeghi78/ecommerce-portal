import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GeoDivisionService } from '../service/geo-division.service';
import { IGeoDivision } from '../geo-division.model';
import { GeoDivisionFormService } from './geo-division-form.service';

import { GeoDivisionUpdateComponent } from './geo-division-update.component';

describe('GeoDivision Management Update Component', () => {
  let comp: GeoDivisionUpdateComponent;
  let fixture: ComponentFixture<GeoDivisionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let geoDivisionFormService: GeoDivisionFormService;
  let geoDivisionService: GeoDivisionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), GeoDivisionUpdateComponent],
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
      .overrideTemplate(GeoDivisionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GeoDivisionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    geoDivisionFormService = TestBed.inject(GeoDivisionFormService);
    geoDivisionService = TestBed.inject(GeoDivisionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call GeoDivision query and add missing value', () => {
      const geoDivision: IGeoDivision = { id: 456 };
      const parent: IGeoDivision = { id: 32678 };
      geoDivision.parent = parent;

      const geoDivisionCollection: IGeoDivision[] = [{ id: 30121 }];
      jest.spyOn(geoDivisionService, 'query').mockReturnValue(of(new HttpResponse({ body: geoDivisionCollection })));
      const additionalGeoDivisions = [parent];
      const expectedCollection: IGeoDivision[] = [...additionalGeoDivisions, ...geoDivisionCollection];
      jest.spyOn(geoDivisionService, 'addGeoDivisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ geoDivision });
      comp.ngOnInit();

      expect(geoDivisionService.query).toHaveBeenCalled();
      expect(geoDivisionService.addGeoDivisionToCollectionIfMissing).toHaveBeenCalledWith(
        geoDivisionCollection,
        ...additionalGeoDivisions.map(expect.objectContaining),
      );
      expect(comp.geoDivisionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const geoDivision: IGeoDivision = { id: 456 };
      const parent: IGeoDivision = { id: 21697 };
      geoDivision.parent = parent;

      activatedRoute.data = of({ geoDivision });
      comp.ngOnInit();

      expect(comp.geoDivisionsSharedCollection).toContain(parent);
      expect(comp.geoDivision).toEqual(geoDivision);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGeoDivision>>();
      const geoDivision = { id: 123 };
      jest.spyOn(geoDivisionFormService, 'getGeoDivision').mockReturnValue(geoDivision);
      jest.spyOn(geoDivisionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoDivision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: geoDivision }));
      saveSubject.complete();

      // THEN
      expect(geoDivisionFormService.getGeoDivision).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(geoDivisionService.update).toHaveBeenCalledWith(expect.objectContaining(geoDivision));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGeoDivision>>();
      const geoDivision = { id: 123 };
      jest.spyOn(geoDivisionFormService, 'getGeoDivision').mockReturnValue({ id: null });
      jest.spyOn(geoDivisionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoDivision: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: geoDivision }));
      saveSubject.complete();

      // THEN
      expect(geoDivisionFormService.getGeoDivision).toHaveBeenCalled();
      expect(geoDivisionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGeoDivision>>();
      const geoDivision = { id: 123 };
      jest.spyOn(geoDivisionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ geoDivision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(geoDivisionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGeoDivision', () => {
      it('Should forward to geoDivisionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(geoDivisionService, 'compareGeoDivision');
        comp.compareGeoDivision(entity, entity2);
        expect(geoDivisionService.compareGeoDivision).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
