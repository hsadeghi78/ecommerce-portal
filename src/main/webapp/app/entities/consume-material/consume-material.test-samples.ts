import { IConsumeMaterial, NewConsumeMaterial } from './consume-material.model';

export const sampleWithRequiredData: IConsumeMaterial = {
  id: 2290,
  typeClassId: 17366,
  name: 'excepting for barring',
  value: 'snowman draw',
};

export const sampleWithPartialData: IConsumeMaterial = {
  id: 214,
  typeClassId: 6895,
  name: 'grateful',
  value: 'instead pen long',
};

export const sampleWithFullData: IConsumeMaterial = {
  id: 23051,
  typeClassId: 6618,
  name: 'near',
  value: 'ulcerate woot',
};

export const sampleWithNewData: NewConsumeMaterial = {
  typeClassId: 28865,
  name: 'value wobbly petition',
  value: 'comfort neglected searchingly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
