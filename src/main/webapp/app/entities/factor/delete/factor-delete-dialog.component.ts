import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFactor } from '../factor.model';
import { FactorService } from '../service/factor.service';

@Component({
  standalone: true,
  templateUrl: './factor-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FactorDeleteDialogComponent {
  factor?: IFactor;

  constructor(
    protected factorService: FactorService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.factorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
