import dayjs from 'dayjs/esm';

export interface IParty {
  id: number;
  title?: string | null;
  partyCode?: string | null;
  tradeTitle?: string | null;
  activationDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
  activationStatus?: boolean | null;
  photo?: string | null;
  photoContentType?: string | null;
  personType?: boolean | null;
}

export type NewParty = Omit<IParty, 'id'> & { id: null };
