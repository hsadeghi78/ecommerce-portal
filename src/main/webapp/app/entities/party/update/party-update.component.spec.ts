import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PartyService } from '../service/party.service';
import { IParty } from '../party.model';
import { PartyFormService } from './party-form.service';

import { PartyUpdateComponent } from './party-update.component';

describe('Party Management Update Component', () => {
  let comp: PartyUpdateComponent;
  let fixture: ComponentFixture<PartyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let partyFormService: PartyFormService;
  let partyService: PartyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PartyUpdateComponent],
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
      .overrideTemplate(PartyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partyFormService = TestBed.inject(PartyFormService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const party: IParty = { id: 456 };

      activatedRoute.data = of({ party });
      comp.ngOnInit();

      expect(comp.party).toEqual(party);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParty>>();
      const party = { id: 123 };
      jest.spyOn(partyFormService, 'getParty').mockReturnValue(party);
      jest.spyOn(partyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ party });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: party }));
      saveSubject.complete();

      // THEN
      expect(partyFormService.getParty).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partyService.update).toHaveBeenCalledWith(expect.objectContaining(party));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParty>>();
      const party = { id: 123 };
      jest.spyOn(partyFormService, 'getParty').mockReturnValue({ id: null });
      jest.spyOn(partyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ party: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: party }));
      saveSubject.complete();

      // THEN
      expect(partyFormService.getParty).toHaveBeenCalled();
      expect(partyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParty>>();
      const party = { id: 123 };
      jest.spyOn(partyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ party });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
