import { IProduct } from 'app/entities/product/product.model';

export interface IUserFavorite {
  id: number;
  product?: Pick<IProduct, 'id' | 'name'> | null;
}

export type NewUserFavorite = Omit<IUserFavorite, 'id'> & { id: null };
