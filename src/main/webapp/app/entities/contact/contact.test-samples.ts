import { IContact, NewContact } from './contact.model';

export const sampleWithRequiredData: IContact = {
  id: 8053,
  contactValue: 'pushy access',
  typeClassId: 8152,
};

export const sampleWithPartialData: IContact = {
  id: 10285,
  contactValue: 'beautifully',
  typeClassId: 12136,
  description: 'nation ha um',
};

export const sampleWithFullData: IContact = {
  id: 20615,
  contactValue: 'tiptoe',
  typeClassId: 21039,
  prefix: 'chubby ',
  description: 'cumbersome jar',
};

export const sampleWithNewData: NewContact = {
  contactValue: 'relate enormously drat',
  typeClassId: 26670,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
