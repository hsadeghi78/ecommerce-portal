import { ILocation } from 'app/entities/location/location.model';
import { IParty } from 'app/entities/party/party.model';

export interface IFactor {
  id: number;
  title?: string | null;
  factorCode?: string | null;
  lastStatusClassId?: number | null;
  paymentStateClassId?: number | null;
  categoryClassId?: number | null;
  totalPrice?: number | null;
  discount?: number | null;
  discountCode?: string | null;
  finalTax?: number | null;
  payable?: number | null;
  description?: string | null;
  location?: Pick<ILocation, 'id'> | null;
  buyerParty?: Pick<IParty, 'id' | 'title'> | null;
  sellerParty?: Pick<IParty, 'id' | 'title'> | null;
}

export type NewFactor = Omit<IFactor, 'id'> & { id: null };
