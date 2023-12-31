export interface IMyAuthority {
  id: number;
  name?: string | null;
  displayName?: string | null;
  parent?: Pick<IMyAuthority, 'id' | 'name'> | null;
}

export type NewMyAuthority = Omit<IMyAuthority, 'id'> & { id: null };
