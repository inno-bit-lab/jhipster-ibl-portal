import dayjs from 'dayjs/esm';

import { IUtente, NewUtente } from './utente.model';

export const sampleWithRequiredData: IUtente = {
  id: 10723,
};

export const sampleWithPartialData: IUtente = {
  id: 22218,
  created: dayjs('2024-02-24'),
  modified: dayjs('2024-02-24'),
  username: 'underground curiously',
  password: 'even voluntarily uh-huh',
  mobile: 'neatly',
  provider: 'huzzah miniature dull',
  attivo: true,
  dataBolocco: dayjs('2024-02-24'),
  lastAccess: dayjs('2024-02-24'),
};

export const sampleWithFullData: IUtente = {
  id: 20023,
  created: dayjs('2024-02-23'),
  modified: dayjs('2024-02-24'),
  username: 'default yet supposing',
  password: 'athwart well-informed',
  mail: 'eek oof',
  mobile: 'heavily instead',
  facebook: 'ooze wealthy',
  google: 'but excitedly',
  instangram: 'via retrospectivity interrogate',
  provider: 'consequently wasteful',
  attivo: true,
  motivoBolocco: 'helplessly into among',
  dataBolocco: dayjs('2024-02-24'),
  registrationDate: dayjs('2024-02-24'),
  lastAccess: dayjs('2024-02-24'),
};

export const sampleWithNewData: NewUtente = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
