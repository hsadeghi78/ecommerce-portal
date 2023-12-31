import { IProduct } from 'app/entities/product/product.model';

export interface IProductItem {
  id: number;
  typeClassId?: number | null;
  name?: string | null;
  value?: string | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
}

export type NewProductItem = Omit<IProductItem, 'id'> & { id: null };
