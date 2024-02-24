import dayjs from 'dayjs/esm';
import { IAzione } from 'app/entities/azione/azione.model';

export interface IRuolo {
  id: number;
  created?: dayjs.Dayjs | null;
  modified?: dayjs.Dayjs | null;
  nomeAzione?: string | null;
  azioni?: IAzione | null;
}

export type NewRuolo = Omit<IRuolo, 'id'> & { id: null };
