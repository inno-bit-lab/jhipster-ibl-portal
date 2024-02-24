import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUtente, NewUtente } from '../utente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtente for edit and NewUtenteFormGroupInput for create.
 */
type UtenteFormGroupInput = IUtente | PartialWithRequiredKeyOf<NewUtente>;

type UtenteFormDefaults = Pick<NewUtente, 'id' | 'attivo'>;

type UtenteFormGroupContent = {
  id: FormControl<IUtente['id'] | NewUtente['id']>;
  created: FormControl<IUtente['created']>;
  modified: FormControl<IUtente['modified']>;
  username: FormControl<IUtente['username']>;
  password: FormControl<IUtente['password']>;
  mail: FormControl<IUtente['mail']>;
  mobile: FormControl<IUtente['mobile']>;
  facebook: FormControl<IUtente['facebook']>;
  google: FormControl<IUtente['google']>;
  instangram: FormControl<IUtente['instangram']>;
  provider: FormControl<IUtente['provider']>;
  attivo: FormControl<IUtente['attivo']>;
  motivoBolocco: FormControl<IUtente['motivoBolocco']>;
  dataBolocco: FormControl<IUtente['dataBolocco']>;
  registrationDate: FormControl<IUtente['registrationDate']>;
  lastAccess: FormControl<IUtente['lastAccess']>;
  ruolo: FormControl<IUtente['ruolo']>;
};

export type UtenteFormGroup = FormGroup<UtenteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtenteFormService {
  createUtenteFormGroup(utente: UtenteFormGroupInput = { id: null }): UtenteFormGroup {
    const utenteRawValue = {
      ...this.getFormDefaults(),
      ...utente,
    };
    return new FormGroup<UtenteFormGroupContent>({
      id: new FormControl(
        { value: utenteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      created: new FormControl(utenteRawValue.created),
      modified: new FormControl(utenteRawValue.modified),
      username: new FormControl(utenteRawValue.username),
      password: new FormControl(utenteRawValue.password),
      mail: new FormControl(utenteRawValue.mail),
      mobile: new FormControl(utenteRawValue.mobile),
      facebook: new FormControl(utenteRawValue.facebook),
      google: new FormControl(utenteRawValue.google),
      instangram: new FormControl(utenteRawValue.instangram),
      provider: new FormControl(utenteRawValue.provider),
      attivo: new FormControl(utenteRawValue.attivo),
      motivoBolocco: new FormControl(utenteRawValue.motivoBolocco),
      dataBolocco: new FormControl(utenteRawValue.dataBolocco),
      registrationDate: new FormControl(utenteRawValue.registrationDate),
      lastAccess: new FormControl(utenteRawValue.lastAccess),
      ruolo: new FormControl(utenteRawValue.ruolo),
    });
  }

  getUtente(form: UtenteFormGroup): IUtente | NewUtente {
    return form.getRawValue() as IUtente | NewUtente;
  }

  resetForm(form: UtenteFormGroup, utente: UtenteFormGroupInput): void {
    const utenteRawValue = { ...this.getFormDefaults(), ...utente };
    form.reset(
      {
        ...utenteRawValue,
        id: { value: utenteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UtenteFormDefaults {
    return {
      id: null,
      attivo: false,
    };
  }
}
