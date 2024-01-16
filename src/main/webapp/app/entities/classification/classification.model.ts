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

export interface IVwClassification {
  id: number;
  title?: string | null;
  classCode?: string | null;
  description?: string | null;
  languageClassId?: number | null;
  typeTitle?: string | null;
  typeCode?: number | null;
  typeDesc?: string | null;
}

export type NewVwClassification = Omit<IVwClassification, 'id'> & { id: null };
