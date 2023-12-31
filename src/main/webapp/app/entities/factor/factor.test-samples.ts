import { IFactor, NewFactor } from './factor.model';

export const sampleWithRequiredData: IFactor = {
  id: 16194,
  title: 'brr upside-down serious',
  factorCode: 'outside',
  lastStatusClassId: 30903,
  paymentStateClassId: 27880,
  totalPrice: 31156.77,
  payable: 6593.81,
};

export const sampleWithPartialData: IFactor = {
  id: 7650,
  title: 'yowza',
  factorCode: 'reside yum',
  lastStatusClassId: 4933,
  paymentStateClassId: 8122,
  categoryClassId: 12506,
  totalPrice: 31615.18,
  discount: 3222.18,
  discountCode: 'competent fly',
  finalTax: 4800,
  payable: 11171.12,
};

export const sampleWithFullData: IFactor = {
  id: 30329,
  title: 'indeed atrophy swim',
  factorCode: 'exempt',
  lastStatusClassId: 20032,
  paymentStateClassId: 7404,
  categoryClassId: 6976,
  totalPrice: 1049.12,
  discount: 23802.48,
  discountCode: 'er clam creative',
  finalTax: 15735.74,
  payable: 21685.9,
  description: 'host mmm ultimately',
};

export const sampleWithNewData: NewFactor = {
  title: 'hard psst',
  factorCode: 'assistance',
  lastStatusClassId: 15007,
  paymentStateClassId: 31058,
  totalPrice: 14144.92,
  payable: 26786.2,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
