import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAzione, NewAzione } from '../azione.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAzione for edit and NewAzioneFormGroupInput for create.
 */
type AzioneFormGroupInput = IAzione | PartialWithRequiredKeyOf<NewAzione>;

type AzioneFormDefaults = Pick<NewAzione, 'id'>;

type AzioneFormGroupContent = {
  id: FormControl<IAzione['id'] | NewAzione['id']>;
  created: FormControl<IAzione['created']>;
  modified: FormControl<IAzione['modified']>;
  nomeAzione: FormControl<IAzione['nomeAzione']>;
  descrizione: FormControl<IAzione['descrizione']>;
};

export type AzioneFormGroup = FormGroup<AzioneFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AzioneFormService {
  createAzioneFormGroup(azione: AzioneFormGroupInput = { id: null }): AzioneFormGroup {
    const azioneRawValue = {
      ...this.getFormDefaults(),
      ...azione,
    };
    return new FormGroup<AzioneFormGroupContent>({
      id: new FormControl(
        { value: azioneRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      created: new FormControl(azioneRawValue.created),
      modified: new FormControl(azioneRawValue.modified),
      nomeAzione: new FormControl(azioneRawValue.nomeAzione),
      descrizione: new FormControl(azioneRawValue.descrizione),
    });
  }

  getAzione(form: AzioneFormGroup): IAzione | NewAzione {
    return form.getRawValue() as IAzione | NewAzione;
  }

  resetForm(form: AzioneFormGroup, azione: AzioneFormGroupInput): void {
    const azioneRawValue = { ...this.getFormDefaults(), ...azione };
    form.reset(
      {
        ...azioneRawValue,
        id: { value: azioneRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AzioneFormDefaults {
    return {
      id: null,
    };
  }
}
