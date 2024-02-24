import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRuolo } from 'app/entities/ruolo/ruolo.model';
import { RuoloService } from 'app/entities/ruolo/service/ruolo.service';
import { IUtente } from '../utente.model';
import { UtenteService } from '../service/utente.service';
import { UtenteFormService, UtenteFormGroup } from './utente-form.service';

@Component({
  standalone: true,
  selector: 'jhi-utente-update',
  templateUrl: './utente-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UtenteUpdateComponent implements OnInit {
  isSaving = false;
  utente: IUtente | null = null;

  ruolosSharedCollection: IRuolo[] = [];

  editForm: UtenteFormGroup = this.utenteFormService.createUtenteFormGroup();

  constructor(
    protected utenteService: UtenteService,
    protected utenteFormService: UtenteFormService,
    protected ruoloService: RuoloService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareRuolo = (o1: IRuolo | null, o2: IRuolo | null): boolean => this.ruoloService.compareRuolo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utente }) => {
      this.utente = utente;
      if (utente) {
        this.updateForm(utente);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utente = this.utenteFormService.getUtente(this.editForm);
    if (utente.id !== null) {
      this.subscribeToSaveResponse(this.utenteService.update(utente));
    } else {
      this.subscribeToSaveResponse(this.utenteService.create(utente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtente>>): void {
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

  protected updateForm(utente: IUtente): void {
    this.utente = utente;
    this.utenteFormService.resetForm(this.editForm, utente);

    this.ruolosSharedCollection = this.ruoloService.addRuoloToCollectionIfMissing<IRuolo>(this.ruolosSharedCollection, utente.ruolo);
  }

  protected loadRelationshipsOptions(): void {
    this.ruoloService
      .query()
      .pipe(map((res: HttpResponse<IRuolo[]>) => res.body ?? []))
      .pipe(map((ruolos: IRuolo[]) => this.ruoloService.addRuoloToCollectionIfMissing<IRuolo>(ruolos, this.utente?.ruolo)))
      .subscribe((ruolos: IRuolo[]) => (this.ruolosSharedCollection = ruolos));
  }
}
