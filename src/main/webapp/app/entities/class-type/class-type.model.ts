export interface IClassType {
  id: number;
  title?: string | null;
  typeCode?: number | null;
  description?: string | null;
}

export type NewClassType = Omit<IClassType, 'id'> & { id: null };
