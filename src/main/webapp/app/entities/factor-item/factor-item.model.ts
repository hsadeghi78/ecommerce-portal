import { IFactor } from 'app/entities/factor/factor.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IFactorItem {
  id: number;
  rowNum?: number | null;
  title?: string | null;
  count?: number | null;
  discount?: number | null;
  tax?: number | null;
  description?: string | null;
  factor?: Pick<IFactor, 'id'> | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
}

export type NewFactorItem = Omit<IFactorItem, 'id'> & { id: null };
