import dayjs from 'dayjs/esm';
import { IFileDocument } from 'app/entities/file-document/file-document.model';
import { ICategory } from 'app/entities/category/category.model';
import { IParty } from 'app/entities/party/party.model';
import { ICampaign } from 'app/entities/campaign/campaign.model';
import { Performance } from 'app/entities/enumerations/performance.model';

export interface IProduct {
  id: number;
  name?: string | null;
  typeClassId?: number | null;
  brandClassId?: number | null;
  sizee?: string | null;
  regularSizeClassId?: number | null;
  languageClassId?: number | null;
  description?: string | null;
  keywords?: string | null;
  photo1?: string | null;
  photo1ContentType?: string | null;
  nationalityClassId?: number | null;
  count?: number | null;
  discount?: number | null;
  originalPrice?: number | null;
  finalPrice?: number | null;
  publishDate?: dayjs.Dayjs | null;
  transportDate?: dayjs.Dayjs | null;
  currencyClassId?: number | null;
  bonus?: number | null;
  warrantyClassId?: number | null;
  deliveryPlaceClassId?: number | null;
  paymentPlaceClassId?: number | null;
  performance?: keyof typeof Performance | null;
  originalityClassId?: number | null;
  satisfaction?: number | null;
  used?: boolean | null;
  documents?: Pick<IFileDocument, 'id'>[] | null;
  category?: Pick<ICategory, 'id' | 'title'> | null;
  party?: Pick<IParty, 'id' | 'title'> | null;
  parent?: Pick<IProduct, 'id' | 'name'> | null;
  campaigns?: Pick<ICampaign, 'id'>[] | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
