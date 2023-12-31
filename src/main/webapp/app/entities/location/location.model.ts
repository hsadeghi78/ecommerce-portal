import { IGeoDivision } from 'app/entities/geo-division/geo-division.model';
import { IParty } from 'app/entities/party/party.model';

export interface ILocation {
  id: number;
  typeClassId?: number | null;
  title?: string | null;
  lat?: number | null;
  lon?: number | null;
  street1?: string | null;
  street2?: string | null;
  street3?: string | null;
  buildingNo?: number | null;
  buildingName?: string | null;
  floor?: number | null;
  unit?: number | null;
  postalCode?: string | null;
  other?: string | null;
  geoDivision?: Pick<IGeoDivision, 'id'> | null;
  party?: Pick<IParty, 'id' | 'title'> | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
