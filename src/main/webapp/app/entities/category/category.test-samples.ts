import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 5712,
  title: 'rudely hm spirited',
  code: 'fooey thud',
  hasChild: true,
  level: 14698,
};

export const sampleWithPartialData: ICategory = {
  id: 8225,
  title: 'confuse',
  code: 'furiously ',
  hasChild: true,
  level: 27310,
};

export const sampleWithFullData: ICategory = {
  id: 23404,
  title: 'muscat spectacular deeply',
  code: 'against',
  hasChild: false,
  level: 13499,
  keywords: 'assault indeed',
  description: 'right surprise',
};

export const sampleWithNewData: NewCategory = {
  title: 'and',
  code: 'youthfully',
  hasChild: false,
  level: 8808,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
