import dayjs from 'dayjs/esm';
import { IRuolo } from 'app/entities/ruolo/ruolo.model';

export interface IAzione {
  id: number;
  created?: dayjs.Dayjs | null;
  modified?: dayjs.Dayjs | null;
  nomeAzione?: string | null;
  descrizione?: string | null;
  ruolos?: IRuolo[] | null;
}

export type NewAzione = Omit<IAzione, 'id'> & { id: null };
