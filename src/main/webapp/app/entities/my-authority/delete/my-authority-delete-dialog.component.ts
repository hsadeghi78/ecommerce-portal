import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMyAuthority } from '../my-authority.model';
import { MyAuthorityService } from '../service/my-authority.service';

@Component({
  standalone: true,
  templateUrl: './my-authority-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MyAuthorityDeleteDialogComponent {
  myAuthority?: IMyAuthority;

  constructor(
    protected myAuthorityService: MyAuthorityService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.myAuthorityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
