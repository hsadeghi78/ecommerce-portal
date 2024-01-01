import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 13442,
  name: 'lobotomise',
  regularSizeClassId: 16384,
  languageClassId: 15556,
  photo1: '../fake-data/blob/hipster.png',
  photo1ContentType: 'unknown',
  count: 12670.44,
  originalPrice: 23603.02,
  finalPrice: 10750.25,
  publishDate: dayjs('2023-12-28'),
  transportDate: dayjs('2023-12-27'),
  currencyClassId: 26074,
  used: true,
};

export const sampleWithPartialData: IProduct = {
  id: 21683,
  name: 'shore',
  regularSizeClassId: 24244,
  languageClassId: 15903,
  photo1: '../fake-data/blob/hipster.png',
  photo1ContentType: 'unknown',
  nationalityClassId: 28647,
  count: 22007.09,
  originalPrice: 19247.26,
  finalPrice: 18072.77,
  publishDate: dayjs('2023-12-28'),
  transportDate: dayjs('2023-12-27'),
  currencyClassId: 21220,
  bonus: 24594.09,
  performance: 'EXCELLENT',
  used: false,
};

export const sampleWithFullData: IProduct = {
  id: 2742,
  name: 'wisely moist whose',
  typeClassId: 31483,
  brandClassId: 23150,
  sizee: 'phew relocate',
  regularSizeClassId: 5379,
  languageClassId: 16769,
  description: 'pish belie',
  keywords: 'in',
  photo1: '../fake-data/blob/hipster.png',
  photo1ContentType: 'unknown',
  nationalityClassId: 16457,
  count: 30022.47,
  discount: 24749.54,
  originalPrice: 14841.51,
  finalPrice: 9768.44,
  publishDate: dayjs('2023-12-27'),
  transportDate: dayjs('2023-12-28'),
  currencyClassId: 29290,
  bonus: 25301.82,
  warrantyClassId: 13522,
  deliveryPlaceClassId: 11352,
  paymentPlaceClassId: 21647,
  performance: 'WEAK',
  originalityClassId: 30449,
  satisfaction: 22415.61,
  used: true,
};

export const sampleWithNewData: NewProduct = {
  name: 'whereas around',
  regularSizeClassId: 11327,
  languageClassId: 18025,
  photo1: '../fake-data/blob/hipster.png',
  photo1ContentType: 'unknown',
  count: 29010.82,
  originalPrice: 4942.21,
  finalPrice: 29065.6,
  publishDate: dayjs('2023-12-28'),
  transportDate: dayjs('2023-12-28'),
  currencyClassId: 18967,
  used: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
