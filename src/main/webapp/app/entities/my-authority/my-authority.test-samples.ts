import { IMyAuthority, NewMyAuthority } from './my-authority.model';

export const sampleWithRequiredData: IMyAuthority = {
  id: 6575,
  name: 'factorize resupply',
  displayName: 'type',
};

export const sampleWithPartialData: IMyAuthority = {
  id: 12973,
  name: 'exclude',
  displayName: 'depict',
};

export const sampleWithFullData: IMyAuthority = {
  id: 7949,
  name: 'youthfully unused',
  displayName: 'finally gadzooks mixed',
};

export const sampleWithNewData: NewMyAuthority = {
  name: 'blaspheme',
  displayName: 'within gah pseudoscience',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
