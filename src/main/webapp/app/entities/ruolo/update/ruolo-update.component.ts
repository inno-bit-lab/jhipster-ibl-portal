import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAzione } from 'app/entities/azione/azione.model';
import { AzioneService } from 'app/entities/azione/service/azione.service';
import { IRuolo } from '../ruolo.model';
import { RuoloService } from '../service/ruolo.service';
import { RuoloFormService, RuoloFormGroup } from './ruolo-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ruolo-update',
  templateUrl: './ruolo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RuoloUpdateComponent implements OnInit {
  isSaving = false;
  ruolo: IRuolo | null = null;

  azionesSharedCollection: IAzione[] = [];

  editForm: RuoloFormGroup = this.ruoloFormService.createRuoloFormGroup();

  constructor(
    protected ruoloService: RuoloService,
    protected ruoloFormService: RuoloFormService,
    protected azioneService: AzioneService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAzione = (o1: IAzione | null, o2: IAzione | null): boolean => this.azioneService.compareAzione(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ruolo }) => {
      this.ruolo = ruolo;
      if (ruolo) {
        this.updateForm(ruolo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ruolo = this.ruoloFormService.getRuolo(this.editForm);
    if (ruolo.id !== null) {
      this.subscribeToSaveResponse(this.ruoloService.update(ruolo));
    } else {
      this.subscribeToSaveResponse(this.ruoloService.create(ruolo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRuolo>>): void {
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

  protected updateForm(ruolo: IRuolo): void {
    this.ruolo = ruolo;
    this.ruoloFormService.resetForm(this.editForm, ruolo);

    this.azionesSharedCollection = this.azioneService.addAzioneToCollectionIfMissing<IAzione>(this.azionesSharedCollection, ruolo.azioni);
  }

  protected loadRelationshipsOptions(): void {
    this.azioneService
      .query()
      .pipe(map((res: HttpResponse<IAzione[]>) => res.body ?? []))
      .pipe(map((aziones: IAzione[]) => this.azioneService.addAzioneToCollectionIfMissing<IAzione>(aziones, this.ruolo?.azioni)))
      .subscribe((aziones: IAzione[]) => (this.azionesSharedCollection = aziones));
  }
}
