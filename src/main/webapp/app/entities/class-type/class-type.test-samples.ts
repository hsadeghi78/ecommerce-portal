import { IClassType, NewClassType } from './class-type.model';

export const sampleWithRequiredData: IClassType = {
  id: 4361,
  title: 'provided per',
  typeCode: 22534,
};

export const sampleWithPartialData: IClassType = {
  id: 4507,
  title: 'yahoo whorl peaceful',
  typeCode: 22224,
  description: 'near',
};

export const sampleWithFullData: IClassType = {
  id: 15597,
  title: 'homeschool happily',
  typeCode: 869,
  description: 'fortunately boo swan',
};

export const sampleWithNewData: NewClassType = {
  title: 'promote',
  typeCode: 19806,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
