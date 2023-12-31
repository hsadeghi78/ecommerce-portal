import { IFactorItem, NewFactorItem } from './factor-item.model';

export const sampleWithRequiredData: IFactorItem = {
  id: 24161,
  rowNum: 23500,
  title: 'fully so',
  count: 22109,
};

export const sampleWithPartialData: IFactorItem = {
  id: 12596,
  rowNum: 20293,
  title: 'conventional consignment given',
  count: 2472,
  discount: 18347.24,
  tax: 14352.25,
};

export const sampleWithFullData: IFactorItem = {
  id: 15256,
  rowNum: 617,
  title: 'briefly catnap monstrous',
  count: 6161,
  discount: 20171.79,
  tax: 24946.74,
  description: 'knotty when',
};

export const sampleWithNewData: NewFactorItem = {
  rowNum: 20376,
  title: 'dish oof',
  count: 18322,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
