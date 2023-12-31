import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IGeoDivision } from 'app/entities/geo-division/geo-division.model';
import { GeoDivisionService } from 'app/entities/geo-division/service/geo-division.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { ILocation } from '../location.model';
import { LocationService } from '../service/location.service';
import { LocationFormService } from './location-form.service';

import { LocationUpdateComponent } from './location-update.component';

describe('Location Management Update Component', () => {
  let comp: LocationUpdateComponent;
  let fixture: ComponentFixture<LocationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let locationFormService: LocationFormService;
  let locationService: LocationService;
  let geoDivisionService: GeoDivisionService;
  let partyService: PartyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), LocationUpdateComponent],
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
      .overrideTemplate(LocationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    locationFormService = TestBed.inject(LocationFormService);
    locationService = TestBed.inject(LocationService);
    geoDivisionService = TestBed.inject(GeoDivisionService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call GeoDivision query and add missing value', () => {
      const location: ILocation = { id: 456 };
      const geoDivision: IGeoDivision = { id: 3595 };
      location.geoDivision = geoDivision;

      const geoDivisionCollection: IGeoDivision[] = [{ id: 23390 }];
      jest.spyOn(geoDivisionService, 'query').mockReturnValue(of(new HttpResponse({ body: geoDivisionCollection })));
      const additionalGeoDivisions = [geoDivision];
      const expectedCollection: IGeoDivision[] = [...additionalGeoDivisions, ...geoDivisionCollection];
      jest.spyOn(geoDivisionService, 'addGeoDivisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ location });
      comp.ngOnInit();

      expect(geoDivisionService.query).toHaveBeenCalled();
      expect(geoDivisionService.addGeoDivisionToCollectionIfMissing).toHaveBeenCalledWith(
        geoDivisionCollection,
        ...additionalGeoDivisions.map(expect.objectContaining),
      );
      expect(comp.geoDivisionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Party query and add missing value', () => {
      const location: ILocation = { id: 456 };
      const party: IParty = { id: 29035 };
      location.party = party;

      const partyCollection: IParty[] = [{ id: 9986 }];
      jest.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      jest.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ location });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(expect.objectContaining),
      );
      expect(comp.partiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const location: ILocation = { id: 456 };
      const geoDivision: IGeoDivision = { id: 14604 };
      location.geoDivision = geoDivision;
      const party: IParty = { id: 1943 };
      location.party = party;

      activatedRoute.data = of({ location });
      comp.ngOnInit();

      expect(comp.geoDivisionsSharedCollection).toContain(geoDivision);
      expect(comp.partiesSharedCollection).toContain(party);
      expect(comp.location).toEqual(location);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocation>>();
      const location = { id: 123 };
      jest.spyOn(locationFormService, 'getLocation').mockReturnValue(location);
      jest.spyOn(locationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ location });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: location }));
      saveSubject.complete();

      // THEN
      expect(locationFormService.getLocation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(locationService.update).toHaveBeenCalledWith(expect.objectContaining(location));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocation>>();
      const location = { id: 123 };
      jest.spyOn(locationFormService, 'getLocation').mockReturnValue({ id: null });
      jest.spyOn(locationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ location: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: location }));
      saveSubject.complete();

      // THEN
      expect(locationFormService.getLocation).toHaveBeenCalled();
      expect(locationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocation>>();
      const location = { id: 123 };
      jest.spyOn(locationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ location });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(locationService.update).toHaveBeenCalled();
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
