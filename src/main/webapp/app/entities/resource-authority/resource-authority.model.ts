import { IResource } from 'app/entities/resource/resource.model';
import { IMyAuthority } from 'app/entities/my-authority/my-authority.model';
import { Verb } from 'app/entities/enumerations/verb.model';

export interface IResourceAuthority {
  id: number;
  verb?: keyof typeof Verb | null;
  resource?: Pick<IResource, 'id' | 'displayName'> | null;
  myAuthority?: Pick<IMyAuthority, 'id' | 'displayName'> | null;
}

export type NewResourceAuthority = Omit<IResourceAuthority, 'id'> & { id: null };
