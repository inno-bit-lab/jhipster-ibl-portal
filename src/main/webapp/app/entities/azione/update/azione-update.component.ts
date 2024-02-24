import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAzione } from '../azione.model';
import { AzioneService } from '../service/azione.service';
import { AzioneFormService, AzioneFormGroup } from './azione-form.service';

@Component({
  standalone: true,
  selector: 'jhi-azione-update',
  templateUrl: './azione-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AzioneUpdateComponent implements OnInit {
  isSaving = false;
  azione: IAzione | null = null;

  editForm: AzioneFormGroup = this.azioneFormService.createAzioneFormGroup();

  constructor(
    protected azioneService: AzioneService,
    protected azioneFormService: AzioneFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ azione }) => {
      this.azione = azione;
      if (azione) {
        this.updateForm(azione);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const azione = this.azioneFormService.getAzione(this.editForm);
    if (azione.id !== null) {
      this.subscribeToSaveResponse(this.azioneService.update(azione));
    } else {
      this.subscribeToSaveResponse(this.azioneService.create(azione));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAzione>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(azione: IAzione): void {
    this.azione = azione;
    this.azioneFormService.resetForm(this.editForm, azione);
  }
}
