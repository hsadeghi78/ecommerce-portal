import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAgreement } from '../agreement.model';
import { AgreementService } from '../service/agreement.service';

@Component({
  standalone: true,
  templateUrl: './agreement-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AgreementDeleteDialogComponent {
  agreement?: IAgreement;

  constructor(
    protected agreementService: AgreementService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.agreementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
