import { IParty } from 'app/entities/party/party.model';

export interface IContact {
  id: number;
  contactValue?: string | null;
  typeClassId?: number | null;
  prefix?: string | null;
  description?: string | null;
  party?: Pick<IParty, 'id' | 'title'> | null;
}

export type NewContact = Omit<IContact, 'id'> & { id: null };
