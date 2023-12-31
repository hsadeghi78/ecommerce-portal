import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { ContactService } from '../service/contact.service';
import { IContact } from '../contact.model';
import { ContactFormService } from './contact-form.service';

import { ContactUpdateComponent } from './contact-update.component';

describe('Contact Management Update Component', () => {
  let comp: ContactUpdateComponent;
  let fixture: ComponentFixture<ContactUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contactFormService: ContactFormService;
  let contactService: ContactService;
  let partyService: PartyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ContactUpdateComponent],
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
      .overrideTemplate(ContactUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContactUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contactFormService = TestBed.inject(ContactFormService);
    contactService = TestBed.inject(ContactService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Party query and add missing value', () => {
      const contact: IContact = { id: 456 };
      const party: IParty = { id: 23161 };
      contact.party = party;

      const partyCollection: IParty[] = [{ id: 27491 }];
      jest.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      jest.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(expect.objectContaining),
      );
      expect(comp.partiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contact: IContact = { id: 456 };
      const party: IParty = { id: 3294 };
      contact.party = party;

      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      expect(comp.partiesSharedCollection).toContain(party);
      expect(comp.contact).toEqual(contact);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContact>>();
      const contact = { id: 123 };
      jest.spyOn(contactFormService, 'getContact').mockReturnValue(contact);
      jest.spyOn(contactService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contact }));
      saveSubject.complete();

      // THEN
      expect(contactFormService.getContact).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contactService.update).toHaveBeenCalledWith(expect.objectContaining(contact));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContact>>();
      const contact = { id: 123 };
      jest.spyOn(contactFormService, 'getContact').mockReturnValue({ id: null });
      jest.spyOn(contactService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contact: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contact }));
      saveSubject.complete();

      // THEN
      expect(contactFormService.getContact).toHaveBeenCalled();
      expect(contactService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContact>>();
      const contact = { id: 123 };
      jest.spyOn(contactService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contactService.update).toHaveBeenCalled();
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
