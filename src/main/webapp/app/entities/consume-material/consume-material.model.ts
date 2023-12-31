import { IProduct } from 'app/entities/product/product.model';

export interface IConsumeMaterial {
  id: number;
  typeClassId?: number | null;
  name?: string | null;
  value?: string | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
}

export type NewConsumeMaterial = Omit<IConsumeMaterial, 'id'> & { id: null };
