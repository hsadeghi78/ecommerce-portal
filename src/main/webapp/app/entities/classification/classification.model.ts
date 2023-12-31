import { IClassType } from 'app/entities/class-type/class-type.model';

export interface IClassification {
  id: number;
  title?: string | null;
  classCode?: string | null;
  description?: string | null;
  languageClassId?: number | null;
  classType?: Pick<IClassType, 'id' | 'title'> | null;
}

export type NewClassification = Omit<IClassification, 'id'> & { id: null };
