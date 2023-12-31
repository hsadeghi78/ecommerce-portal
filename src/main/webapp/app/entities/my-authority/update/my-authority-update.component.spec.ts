import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MyAuthorityService } from '../service/my-authority.service';
import { IMyAuthority } from '../my-authority.model';
import { MyAuthorityFormService } from './my-authority-form.service';

import { MyAuthorityUpdateComponent } from './my-authority-update.component';

describe('MyAuthority Management Update Component', () => {
  let comp: MyAuthorityUpdateComponent;
  let fixture: ComponentFixture<MyAuthorityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let myAuthorityFormService: MyAuthorityFormService;
  let myAuthorityService: MyAuthorityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MyAuthorityUpdateComponent],
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
      .overrideTemplate(MyAuthorityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MyAuthorityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    myAuthorityFormService = TestBed.inject(MyAuthorityFormService);
    myAuthorityService = TestBed.inject(MyAuthorityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MyAuthority query and add missing value', () => {
      const myAuthority: IMyAuthority = { id: 456 };
      const parent: IMyAuthority = { id: 9567 };
      myAuthority.parent = parent;

      const myAuthorityCollection: IMyAuthority[] = [{ id: 32429 }];
      jest.spyOn(myAuthorityService, 'query').mockReturnValue(of(new HttpResponse({ body: myAuthorityCollection })));
      const additionalMyAuthorities = [parent];
      const expectedCollection: IMyAuthority[] = [...additionalMyAuthorities, ...myAuthorityCollection];
      jest.spyOn(myAuthorityService, 'addMyAuthorityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ myAuthority });
      comp.ngOnInit();

      expect(myAuthorityService.query).toHaveBeenCalled();
      expect(myAuthorityService.addMyAuthorityToCollectionIfMissing).toHaveBeenCalledWith(
        myAuthorityCollection,
        ...additionalMyAuthorities.map(expect.objectContaining),
      );
      expect(comp.myAuthoritiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const myAuthority: IMyAuthority = { id: 456 };
      const parent: IMyAuthority = { id: 25133 };
      myAuthority.parent = parent;

      activatedRoute.data = of({ myAuthority });
      comp.ngOnInit();

      expect(comp.myAuthoritiesSharedCollection).toContain(parent);
      expect(comp.myAuthority).toEqual(myAuthority);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMyAuthority>>();
      const myAuthority = { id: 123 };
      jest.spyOn(myAuthorityFormService, 'getMyAuthority').mockReturnValue(myAuthority);
      jest.spyOn(myAuthorityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ myAuthority });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: myAuthority }));
      saveSubject.complete();

      // THEN
      expect(myAuthorityFormService.getMyAuthority).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(myAuthorityService.update).toHaveBeenCalledWith(expect.objectContaining(myAuthority));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMyAuthority>>();
      const myAuthority = { id: 123 };
      jest.spyOn(myAuthorityFormService, 'getMyAuthority').mockReturnValue({ id: null });
      jest.spyOn(myAuthorityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ myAuthority: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: myAuthority }));
      saveSubject.complete();

      // THEN
      expect(myAuthorityFormService.getMyAuthority).toHaveBeenCalled();
      expect(myAuthorityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMyAuthority>>();
      const myAuthority = { id: 123 };
      jest.spyOn(myAuthorityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ myAuthority });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(myAuthorityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMyAuthority', () => {
      it('Should forward to myAuthorityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(myAuthorityService, 'compareMyAuthority');
        comp.compareMyAuthority(entity, entity2);
        expect(myAuthorityService.compareMyAuthority).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
