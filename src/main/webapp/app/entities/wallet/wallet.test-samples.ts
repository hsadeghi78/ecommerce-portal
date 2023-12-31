import { IWallet, NewWallet } from './wallet.model';

export const sampleWithRequiredData: IWallet = {
  id: 5448,
  stock: 27577.21,
};

export const sampleWithPartialData: IWallet = {
  id: 32342,
  transTypeClassId: 14980,
  stock: 12688.39,
  deposit: 12856.85,
};

export const sampleWithFullData: IWallet = {
  id: 16264,
  transTypeClassId: 9575,
  stock: 1237.54,
  description: 'savor',
  deposit: 6880.08,
  withdrawal: 22903.51,
};

export const sampleWithNewData: NewWallet = {
  stock: 19691.3,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
