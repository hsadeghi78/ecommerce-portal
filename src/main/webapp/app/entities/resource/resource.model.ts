import { ResourceType } from 'app/entities/enumerations/resource-type.model';

export interface IResource {
  id: number;
  name?: string | null;
  displayName?: string | null;
  apiUri?: string | null;
  resourceType?: keyof typeof ResourceType | null;
}

export type NewResource = Omit<IResource, 'id'> & { id: null };
