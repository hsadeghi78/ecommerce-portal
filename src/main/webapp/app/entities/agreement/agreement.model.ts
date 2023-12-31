import dayjs from 'dayjs/esm';
import { IParty } from 'app/entities/party/party.model';

export interface IAgreement {
  id: number;
  name?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  activationStatusClassId?: number | null;
  infrastructureBenefit?: number | null;
  extraBenefit?: number | null;
  provider?: Pick<IParty, 'id' | 'title'> | null;
  consumer?: Pick<IParty, 'id' | 'title'> | null;
}

export type NewAgreement = Omit<IAgreement, 'id'> & { id: null };
