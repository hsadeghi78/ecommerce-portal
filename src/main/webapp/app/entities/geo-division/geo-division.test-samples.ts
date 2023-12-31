import { IGeoDivision, NewGeoDivision } from './geo-division.model';

export const sampleWithRequiredData: IGeoDivision = {
  id: 875,
  name: 'perfect micronutrient kindness',
  code: 28544,
  level: 3553,
};

export const sampleWithPartialData: IGeoDivision = {
  id: 17627,
  name: 'analyse ugh exhibition',
  code: 8358,
  level: 6262,
};

export const sampleWithFullData: IGeoDivision = {
  id: 8038,
  name: 'gadzooks',
  code: 21809,
  level: 31560,
};

export const sampleWithNewData: NewGeoDivision = {
  name: 'what frenetically never',
  code: 11586,
  level: 4607,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
