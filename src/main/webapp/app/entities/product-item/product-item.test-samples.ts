import { IProductItem, NewProductItem } from './product-item.model';

export const sampleWithRequiredData: IProductItem = {
  id: 19928,
  typeClassId: 18758,
  name: 'ick',
  value: 'if likewise',
};

export const sampleWithPartialData: IProductItem = {
  id: 5765,
  typeClassId: 28394,
  name: 'yum',
  value: 'next suspiciously',
};

export const sampleWithFullData: IProductItem = {
  id: 18517,
  typeClassId: 3385,
  name: 'displacement flawless',
  value: 'cautiously',
};

export const sampleWithNewData: NewProductItem = {
  typeClassId: 1862,
  name: 'coarse',
  value: 'pro',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
