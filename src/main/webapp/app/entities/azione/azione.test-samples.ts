import dayjs from 'dayjs/esm';

import { IAzione, NewAzione } from './azione.model';

export const sampleWithRequiredData: IAzione = {
  id: 2525,
};

export const sampleWithPartialData: IAzione = {
  id: 22920,
  created: dayjs('2024-02-24'),
  modified: dayjs('2024-02-24'),
  nomeAzione: 'obtrude what',
  descrizione: 'brr',
};

export const sampleWithFullData: IAzione = {
  id: 17489,
  created: dayjs('2024-02-24'),
  modified: dayjs('2024-02-24'),
  nomeAzione: 'hm openly familiar',
  descrizione: 'which closely joyously',
};

export const sampleWithNewData: NewAzione = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
