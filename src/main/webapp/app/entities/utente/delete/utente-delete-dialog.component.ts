import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUtente } from '../utente.model';
import { UtenteService } from '../service/utente.service';

@Component({
  standalone: true,
  templateUrl: './utente-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UtenteDeleteDialogComponent {
  utente?: IUtente;

  constructor(
    protected utenteService: UtenteService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.utenteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
