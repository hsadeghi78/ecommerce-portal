import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { AgreementService } from '../service/agreement.service';
import { IAgreement } from '../agreement.model';
import { AgreementFormService } from './agreement-form.service';

import { AgreementUpdateComponent } from './agreement-update.component';

describe('Agreement Management Update Component', () => {
  let comp: AgreementUpdateComponent;
  let fixture: ComponentFixture<AgreementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agreementFormService: AgreementFormService;
  let agreementService: AgreementService;
  let partyService: PartyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AgreementUpdateComponent],
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
      .overrideTemplate(AgreementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgreementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agreementFormService = TestBed.inject(AgreementFormService);
    agreementService = TestBed.inject(AgreementService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Party query and add missing value', () => {
      const agreement: IAgreement = { id: 456 };
      const provider: IParty = { id: 23068 };
      agreement.provider = provider;
      const consumer: IParty = { id: 31321 };
      agreement.consumer = consumer;

      const partyCollection: IParty[] = [{ id: 12060 }];
      jest.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [provider, consumer];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      jest.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agreement });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(expect.objectContaining),
      );
      expect(comp.partiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const agreement: IAgreement = { id: 456 };
      const provider: IParty = { id: 32140 };
      agreement.provider = provider;
      const consumer: IParty = { id: 15104 };
      agreement.consumer = consumer;

      activatedRoute.data = of({ agreement });
      comp.ngOnInit();

      expect(comp.partiesSharedCollection).toContain(provider);
      expect(comp.partiesSharedCollection).toContain(consumer);
      expect(comp.agreement).toEqual(agreement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgreement>>();
      const agreement = { id: 123 };
      jest.spyOn(agreementFormService, 'getAgreement').mockReturnValue(agreement);
      jest.spyOn(agreementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agreement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agreement }));
      saveSubject.complete();

      // THEN
      expect(agreementFormService.getAgreement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(agreementService.update).toHaveBeenCalledWith(expect.objectContaining(agreement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgreement>>();
      const agreement = { id: 123 };
      jest.spyOn(agreementFormService, 'getAgreement').mockReturnValue({ id: null });
      jest.spyOn(agreementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agreement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agreement }));
      saveSubject.complete();

      // THEN
      expect(agreementFormService.getAgreement).toHaveBeenCalled();
      expect(agreementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgreement>>();
      const agreement = { id: 123 };
      jest.spyOn(agreementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agreement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agreementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareParty', () => {
      it('Should forward to partyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
