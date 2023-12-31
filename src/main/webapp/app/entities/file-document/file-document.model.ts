import { IProduct } from 'app/entities/product/product.model';

export interface IFileDocument {
  id: number;
  fileName?: string | null;
  fileContent?: string | null;
  fileContentContentType?: string | null;
  filePath?: string | null;
  description?: string | null;
  prices?: Pick<IProduct, 'id'>[] | null;
}

export type NewFileDocument = Omit<IFileDocument, 'id'> & { id: null };
