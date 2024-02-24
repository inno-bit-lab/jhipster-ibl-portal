import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRuolo } from '../ruolo.model';
import { RuoloService } from '../service/ruolo.service';

@Component({
  standalone: true,
  templateUrl: './ruolo-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RuoloDeleteDialogComponent {
  ruolo?: IRuolo;

  constructor(
    protected ruoloService: RuoloService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ruoloService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
