import { ICriticism, NewCriticism } from './criticism.model';

export const sampleWithRequiredData: ICriticism = {
  id: 32631,
  fullName: 'alongside murky',
  description: 'till',
};

export const sampleWithPartialData: ICriticism = {
  id: 29814,
  fullName: 'anti rectangle unto',
  contactNumber: 'ack',
  description: 'oh',
};

export const sampleWithFullData: ICriticism = {
  id: 29478,
  fullName: 'off',
  email: 'Raphael3@hotmail.com',
  contactNumber: 'bare ischemia',
  description: 'whenever puzzling next',
};

export const sampleWithNewData: NewCriticism = {
  fullName: 'pfft reproachfully',
  description: 'that',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
