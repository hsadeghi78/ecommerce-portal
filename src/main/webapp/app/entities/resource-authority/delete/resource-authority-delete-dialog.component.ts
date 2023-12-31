import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IResourceAuthority } from '../resource-authority.model';
import { ResourceAuthorityService } from '../service/resource-authority.service';

@Component({
  standalone: true,
  templateUrl: './resource-authority-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ResourceAuthorityDeleteDialogComponent {
  resourceAuthority?: IResourceAuthority;

  constructor(
    protected resourceAuthorityService: ResourceAuthorityService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resourceAuthorityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
