import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../party.test-samples';

import { PartyFormService } from './party-form.service';

describe('Party Form Service', () => {
  let service: PartyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PartyFormService);
  });

  describe('Service methods', () => {
    describe('createPartyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPartyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            partyCode: expect.any(Object),
            tradeTitle: expect.any(Object),
            activationDate: expect.any(Object),
            expirationDate: expect.any(Object),
            activationStatus: expect.any(Object),
            photo: expect.any(Object),
            personType: expect.any(Object),
          }),
        );
      });

      it('passing IParty should create a new form with FormGroup', () => {
        const formGroup = service.createPartyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            partyCode: expect.any(Object),
            tradeTitle: expect.any(Object),
            activationDate: expect.any(Object),
            expirationDate: expect.any(Object),
            activationStatus: expect.any(Object),
            photo: expect.any(Object),
            personType: expect.any(Object),
          }),
        );
      });
    });

    describe('getParty', () => {
      it('should return NewParty for default Party initial value', () => {
        const formGroup = service.createPartyFormGroup(sampleWithNewData);

        const party = service.getParty(formGroup) as any;

        expect(party).toMatchObject(sampleWithNewData);
      });

      it('should return NewParty for empty Party initial value', () => {
        const formGroup = service.createPartyFormGroup();

        const party = service.getParty(formGroup) as any;

        expect(party).toMatchObject({});
      });

      it('should return IParty', () => {
        const formGroup = service.createPartyFormGroup(sampleWithRequiredData);

        const party = service.getParty(formGroup) as any;

        expect(party).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParty should not enable id FormControl', () => {
        const formGroup = service.createPartyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParty should disable id FormControl', () => {
        const formGroup = service.createPartyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
