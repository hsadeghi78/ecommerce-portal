import dayjs from 'dayjs/esm';

import { IAgreement, NewAgreement } from './agreement.model';

export const sampleWithRequiredData: IAgreement = {
  id: 5391,
  name: 'meaningfully badly',
  startDate: dayjs('2023-12-28'),
  endDate: dayjs('2023-12-27'),
  activationStatusClassId: 2003,
  infrastructureBenefit: 16837.11,
};

export const sampleWithPartialData: IAgreement = {
  id: 16269,
  name: 'spleen rapid',
  startDate: dayjs('2023-12-27'),
  endDate: dayjs('2023-12-28'),
  activationStatusClassId: 15270,
  infrastructureBenefit: 26653.98,
};

export const sampleWithFullData: IAgreement = {
  id: 14547,
  name: 'after',
  startDate: dayjs('2023-12-27'),
  endDate: dayjs('2023-12-27'),
  activationStatusClassId: 31327,
  infrastructureBenefit: 22704.4,
  extraBenefit: 22292.12,
};

export const sampleWithNewData: NewAgreement = {
  name: 'hence afraid where',
  startDate: dayjs('2023-12-27'),
  endDate: dayjs('2023-12-27'),
  activationStatusClassId: 4984,
  infrastructureBenefit: 2729.47,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
