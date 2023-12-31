import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IConsumeMaterial } from '../consume-material.model';
import { ConsumeMaterialService } from '../service/consume-material.service';

@Component({
  standalone: true,
  templateUrl: './consume-material-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConsumeMaterialDeleteDialogComponent {
  consumeMaterial?: IConsumeMaterial;

  constructor(
    protected consumeMaterialService: ConsumeMaterialService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.consumeMaterialService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
