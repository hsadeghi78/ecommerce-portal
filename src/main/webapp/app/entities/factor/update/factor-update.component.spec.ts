import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IFactor } from '../factor.model';
import { FactorService } from '../service/factor.service';
import { FactorFormService } from './factor-form.service';

import { FactorUpdateComponent } from './factor-update.component';

describe('Factor Management Update Component', () => {
  let comp: FactorUpdateComponent;
  let fixture: ComponentFixture<FactorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let factorFormService: FactorFormService;
  let factorService: FactorService;
  let locationService: LocationService;
  let partyService: PartyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FactorUpdateComponent],
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
      .overrideTemplate(FactorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FactorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    factorFormService = TestBed.inject(FactorFormService);
    factorService = TestBed.inject(FactorService);
    locationService = TestBed.inject(LocationService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call location query and add missing value', () => {
      const factor: IFactor = { id: 456 };
      const location: ILocation = { id: 122 };
      factor.location = location;

      const locationCollection: ILocation[] = [{ id: 310 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const expectedCollection: ILocation[] = [location, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factor });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(locationCollection, location);
      expect(comp.locationsCollection).toEqual(expectedCollection);
    });

    it('Should call Party query and add missing value', () => {
      const factor: IFactor = { id: 456 };
      const buyerParty: IParty = { id: 23978 };
      factor.buyerParty = buyerParty;
      const sellerParty: IParty = { id: 30990 };
      factor.sellerParty = sellerParty;

      const partyCollection: IParty[] = [{ id: 15602 }];
      jest.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [buyerParty, sellerParty];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      jest.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factor });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(expect.objectContaining),
      );
      expect(comp.partiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const factor: IFactor = { id: 456 };
      const location: ILocation = { id: 10734 };
      factor.location = location;
      const buyerParty: IParty = { id: 28600 };
      factor.buyerParty = buyerParty;
      const sellerParty: IParty = { id: 30158 };
      factor.sellerParty = sellerParty;

      activatedRoute.data = of({ factor });
      comp.ngOnInit();

      expect(comp.locationsCollection).toContain(location);
      expect(comp.partiesSharedCollection).toContain(buyerParty);
      expect(comp.partiesSharedCollection).toContain(sellerParty);
      expect(comp.factor).toEqual(factor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactor>>();
      const factor = { id: 123 };
      jest.spyOn(factorFormService, 'getFactor').mockReturnValue(factor);
      jest.spyOn(factorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factor }));
      saveSubject.complete();

      // THEN
      expect(factorFormService.getFactor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(factorService.update).toHaveBeenCalledWith(expect.objectContaining(factor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactor>>();
      const factor = { id: 123 };
      jest.spyOn(factorFormService, 'getFactor').mockReturnValue({ id: null });
      jest.spyOn(factorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factor }));
      saveSubject.complete();

      // THEN
      expect(factorFormService.getFactor).toHaveBeenCalled();
      expect(factorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactor>>();
      const factor = { id: 123 };
      jest.spyOn(factorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(factorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLocation', () => {
      it('Should forward to locationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(locationService, 'compareLocation');
        comp.compareLocation(entity, entity2);
        expect(locationService.compareLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
