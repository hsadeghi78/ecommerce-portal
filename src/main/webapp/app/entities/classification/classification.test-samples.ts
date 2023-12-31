import { IClassification, NewClassification } from './classification.model';

export const sampleWithRequiredData: IClassification = {
  id: 22598,
  title: 'above majestic extra-small',
  classCode: 'though yieldingly sad',
  languageClassId: 1850,
};

export const sampleWithPartialData: IClassification = {
  id: 10741,
  title: 'impossible pertinent',
  classCode: 'rectify pfft',
  description: 'gah prime repeatedly',
  languageClassId: 18871,
};

export const sampleWithFullData: IClassification = {
  id: 17216,
  title: 'smoothly',
  classCode: 'hospitable psst',
  description: 'eek aw',
  languageClassId: 30549,
};

export const sampleWithNewData: NewClassification = {
  title: 'within during readily',
  classCode: 'silky',
  languageClassId: 13485,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
