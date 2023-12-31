import { IResourceAuthority, NewResourceAuthority } from './resource-authority.model';

export const sampleWithRequiredData: IResourceAuthority = {
  id: 11303,
  verb: 'VIEW',
};

export const sampleWithPartialData: IResourceAuthority = {
  id: 23237,
  verb: 'NO_GRANT',
};

export const sampleWithFullData: IResourceAuthority = {
  id: 15150,
  verb: 'VIEW',
};

export const sampleWithNewData: NewResourceAuthority = {
  verb: 'DELETE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
