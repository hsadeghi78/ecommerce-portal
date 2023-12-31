import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFactorItem } from '../factor-item.model';
import { FactorItemService } from '../service/factor-item.service';

@Component({
  standalone: true,
  templateUrl: './factor-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FactorItemDeleteDialogComponent {
  factorItem?: IFactorItem;

  constructor(
    protected factorItemService: FactorItemService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.factorItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
