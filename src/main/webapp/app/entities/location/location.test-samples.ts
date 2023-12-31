import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 28507,
  typeClassId: 27998,
  title: 'after cruelly',
  lat: 26499.02,
  lon: 6329.1,
  buildingNo: 7712,
  unit: 32684,
  postalCode: 'psst smart',
};

export const sampleWithPartialData: ILocation = {
  id: 11632,
  typeClassId: 4131,
  title: 'adventurously hone diligently',
  lat: 12910.24,
  lon: 1990.7,
  street1: 'sandpaper wonder disintegrate',
  buildingNo: 15570,
  buildingName: 'famously inasmuch hence',
  unit: 19178,
  postalCode: 'than mid spa',
  other: 'commingle unkempt trust',
};

export const sampleWithFullData: ILocation = {
  id: 31674,
  typeClassId: 28628,
  title: 'dwell concerning',
  lat: 19222.37,
  lon: 13819.26,
  street1: 'furrow',
  street2: 'father tall impressionable',
  street3: 'lest yum',
  buildingNo: 15293,
  buildingName: 'stress provided',
  floor: 23848,
  unit: 15833,
  postalCode: 'scrutinize',
  other: 'interestingly damp',
};

export const sampleWithNewData: NewLocation = {
  typeClassId: 4774,
  title: 'fickle besides',
  lat: 8501.46,
  lon: 14610.98,
  buildingNo: 23336,
  unit: 30464,
  postalCode: 'before for',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
