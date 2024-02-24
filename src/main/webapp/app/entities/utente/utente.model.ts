import dayjs from 'dayjs/esm';
import { IRuolo } from 'app/entities/ruolo/ruolo.model';

export interface IUtente {
  id: number;
  created?: dayjs.Dayjs | null;
  modified?: dayjs.Dayjs | null;
  username?: string | null;
  password?: string | null;
  mail?: string | null;
  mobile?: string | null;
  facebook?: string | null;
  google?: string | null;
  instangram?: string | null;
  provider?: string | null;
  attivo?: boolean | null;
  motivoBolocco?: string | null;
  dataBolocco?: dayjs.Dayjs | null;
  registrationDate?: dayjs.Dayjs | null;
  lastAccess?: dayjs.Dayjs | null;
  ruolo?: IRuolo | null;
}

export type NewUtente = Omit<IUtente, 'id'> & { id: null };
