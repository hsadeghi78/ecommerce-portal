import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 20712,
  name: 'fellow about',
  regularSizeClassId: 29283,
  languageClassId: 26074,
  photo1: '../fake-data/blob/hipster.png',
  photo1ContentType: 'unknown',
  count: 11257.66,
  originalPrice: 27128.87,
  finalPrice: 21383.45,
  publishDate: dayjs('2023-12-27'),
  transportDate: dayjs('2023-12-27'),
  currencyClassId: 25432,
  used: true,
};

export const sampleWithPartialData: IProduct = {
  id: 29403,
  name: 'only',
  brandClassId: 18073,
  regularSizeClassId: 3593,
  languageClassId: 20415,
  photo1: '../fake-data/blob/hipster.png',
  photo1ContentType: 'unknown',
  count: 21219.75,
  discount: 24594.09,
  originalPrice: 29699,
  finalPrice: 30403.24,
  publishDate: dayjs('2023-12-28'),
  transportDate: dayjs('2023-12-27'),
  currencyClassId: 2622,
  paymentPlaceClassId: 16316,
  satisfaction: 2353.17,
  used: true,
};

export const sampleWithFullData: IProduct = {
  id: 27243,
  name: 'stiff unlike',
  typeClassId: 31401,
  brandClassId: 31483,
  sizee: 'as tuck concuss',
  regularSizeClassId: 3115,
  languageClassId: 31410,
  description: 'opposite drat gleefully',
  keywords: 'crick yowza why',
  photo1: '../fake-data/blob/hipster.png',
  photo1ContentType: 'unknown',
  count: 18255.99,
  discount: 20236.33,
  originalPrice: 7215.08,
  finalPrice: 6455.52,
  publishDate: dayjs('2023-12-28'),
  transportDate: dayjs('2023-12-27'),
  currencyClassId: 29011,
  bonus: 4942.21,
  warrantyClassId: 29066,
  deliveryPlaceClassId: 4467,
  paymentPlaceClassId: 7301,
  performance: 'WELL',
  originalityClassId: 23844,
  satisfaction: 12171.79,
  used: false,
};

export const sampleWithNewData: NewProduct = {
  name: 'pull absorb unbearably',
  regularSizeClassId: 9086,
  languageClassId: 9948,
  photo1: '../fake-data/blob/hipster.png',
  photo1ContentType: 'unknown',
  count: 11388.05,
  originalPrice: 29154.87,
  finalPrice: 25537.68,
  publishDate: dayjs('2023-12-27'),
  transportDate: dayjs('2023-12-28'),
  currencyClassId: 13012,
  used: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
