export interface ICategory {
  id: number;
  title?: string | null;
  code?: string | null;
  hasChild?: boolean | null;
  level?: number | null;
  keywords?: string | null;
  description?: string | null;
  parent?: Pick<ICategory, 'id' | 'title'> | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
