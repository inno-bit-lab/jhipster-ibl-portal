import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRuolo, NewRuolo } from '../ruolo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRuolo for edit and NewRuoloFormGroupInput for create.
 */
type RuoloFormGroupInput = IRuolo | PartialWithRequiredKeyOf<NewRuolo>;

type RuoloFormDefaults = Pick<NewRuolo, 'id'>;

type RuoloFormGroupContent = {
  id: FormControl<IRuolo['id'] | NewRuolo['id']>;
  created: FormControl<IRuolo['created']>;
  modified: FormControl<IRuolo['modified']>;
  nomeAzione: FormControl<IRuolo['nomeAzione']>;
  azioni: FormControl<IRuolo['azioni']>;
};

export type RuoloFormGroup = FormGroup<RuoloFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RuoloFormService {
  createRuoloFormGroup(ruolo: RuoloFormGroupInput = { id: null }): RuoloFormGroup {
    const ruoloRawValue = {
      ...this.getFormDefaults(),
      ...ruolo,
    };
    return new FormGroup<RuoloFormGroupContent>({
      id: new FormControl(
        { value: ruoloRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      created: new FormControl(ruoloRawValue.created),
      modified: new FormControl(ruoloRawValue.modified),
      nomeAzione: new FormControl(ruoloRawValue.nomeAzione),
      azioni: new FormControl(ruoloRawValue.azioni),
    });
  }

  getRuolo(form: RuoloFormGroup): IRuolo | NewRuolo {
    return form.getRawValue() as IRuolo | NewRuolo;
  }

  resetForm(form: RuoloFormGroup, ruolo: RuoloFormGroupInput): void {
    const ruoloRawValue = { ...this.getFormDefaults(), ...ruolo };
    form.reset(
      {
        ...ruoloRawValue,
        id: { value: ruoloRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RuoloFormDefaults {
    return {
      id: null,
    };
  }
}
