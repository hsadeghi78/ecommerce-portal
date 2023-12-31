import { IResource, NewResource } from './resource.model';

export const sampleWithRequiredData: IResource = {
  id: 11821,
  name: 'midline beneath',
  displayName: 'phew',
  apiUri: 'sprout',
  resourceType: 'PAGE_URL',
};

export const sampleWithPartialData: IResource = {
  id: 24312,
  name: 'gently between gee',
  displayName: 'for yahoo hydrant',
  apiUri: 'or secularise',
  resourceType: 'COMPONENT',
};

export const sampleWithFullData: IResource = {
  id: 22778,
  name: 'mecca',
  displayName: 'fruitful',
  apiUri: 'since fib',
  resourceType: 'COMPONENT',
};

export const sampleWithNewData: NewResource = {
  name: 'witty neatly',
  displayName: 'immediate phooey',
  apiUri: 'gah flowery',
  resourceType: 'PAGE_URL',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
