import dayjs from 'dayjs/esm';

import { ICampaign, NewCampaign } from './campaign.model';

export const sampleWithRequiredData: ICampaign = {
  id: 26315,
  title: 'mmm or',
  startDate: dayjs('2023-12-28'),
  endDate: dayjs('2023-12-28'),
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
};

export const sampleWithPartialData: ICampaign = {
  id: 1723,
  title: 'breakable nor',
  startDate: dayjs('2023-12-28'),
  endDate: dayjs('2023-12-28'),
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  description: 'insistent flash',
};

export const sampleWithFullData: ICampaign = {
  id: 29556,
  title: 'amend',
  startDate: dayjs('2023-12-27'),
  endDate: dayjs('2023-12-28'),
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  description: 'yahoo when',
};

export const sampleWithNewData: NewCampaign = {
  title: 'geez but gah',
  startDate: dayjs('2023-12-28'),
  endDate: dayjs('2023-12-27'),
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
