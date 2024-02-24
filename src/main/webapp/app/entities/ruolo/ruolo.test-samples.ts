import dayjs from 'dayjs/esm';

import { IRuolo, NewRuolo } from './ruolo.model';

export const sampleWithRequiredData: IRuolo = {
  id: 11030,
};

export const sampleWithPartialData: IRuolo = {
  id: 14342,
  modified: dayjs('2024-02-24'),
};

export const sampleWithFullData: IRuolo = {
  id: 8334,
  created: dayjs('2024-02-23'),
  modified: dayjs('2024-02-24'),
  nomeAzione: 'per',
};

export const sampleWithNewData: NewRuolo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
