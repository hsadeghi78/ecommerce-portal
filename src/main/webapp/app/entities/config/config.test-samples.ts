import { IConfig, NewConfig } from './config.model';

export const sampleWithRequiredData: IConfig = {
  id: 23833,
};

export const sampleWithPartialData: IConfig = {
  id: 5140,
};

export const sampleWithFullData: IConfig = {
  id: 14243,
  displayName: 'or footnote',
  code: 'unruly',
  value: 'amid between',
};

export const sampleWithNewData: NewConfig = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
