import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICriticism } from '../criticism.model';
import { CriticismService } from '../service/criticism.service';

@Component({
  standalone: true,
  templateUrl: './criticism-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CriticismDeleteDialogComponent {
  criticism?: ICriticism;

  constructor(
    protected criticismService: CriticismService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.criticismService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
