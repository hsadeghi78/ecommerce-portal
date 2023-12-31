import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';

export interface ICampaign {
  id: number;
  title?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  photo?: string | null;
  photoContentType?: string | null;
  description?: string | null;
  products?: Pick<IProduct, 'id'>[] | null;
}

export type NewCampaign = Omit<ICampaign, 'id'> & { id: null };
