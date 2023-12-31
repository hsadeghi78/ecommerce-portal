export interface IGeoDivision {
  id: number;
  name?: string | null;
  code?: number | null;
  level?: number | null;
  parent?: Pick<IGeoDivision, 'id' | 'name'> | null;
}

export type NewGeoDivision = Omit<IGeoDivision, 'id'> & { id: null };
