import dayjs from 'dayjs/esm';

import { IParty, NewParty } from './party.model';

export const sampleWithRequiredData: IParty = {
  id: 25441,
  title: 'yippee',
  partyCode: 'excluding interleave which',
  tradeTitle: 'until',
  activationDate: dayjs('2023-12-27'),
  activationStatus: false,
  personType: true,
};

export const sampleWithPartialData: IParty = {
  id: 25550,
  title: 'spectacular',
  partyCode: 'wave',
  tradeTitle: 'oh screen',
  activationDate: dayjs('2023-12-28'),
  expirationDate: dayjs('2023-12-27'),
  activationStatus: true,
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  personType: true,
};

export const sampleWithFullData: IParty = {
  id: 27540,
  title: 'defy ew',
  partyCode: 'opposite',
  tradeTitle: 'backfire',
  activationDate: dayjs('2023-12-27'),
  expirationDate: dayjs('2023-12-27'),
  activationStatus: true,
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  personType: true,
};

export const sampleWithNewData: NewParty = {
  title: 'digital actually',
  partyCode: 'eliminate sans teapot',
  tradeTitle: 'considering halve contrast',
  activationDate: dayjs('2023-12-28'),
  activationStatus: false,
  personType: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
