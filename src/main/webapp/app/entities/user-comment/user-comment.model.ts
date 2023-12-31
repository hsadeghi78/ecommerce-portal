import { IParty } from 'app/entities/party/party.model';
import { IProduct } from 'app/entities/product/product.model';
import { IFactor } from 'app/entities/factor/factor.model';

export interface IUserComment {
  id: number;
  rating?: number | null;
  visible?: boolean | null;
  description?: string | null;
  party?: Pick<IParty, 'id' | 'title'> | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
  factor?: Pick<IFactor, 'id'> | null;
  parent?: Pick<IUserComment, 'id'> | null;
}

export type NewUserComment = Omit<IUserComment, 'id'> & { id: null };
