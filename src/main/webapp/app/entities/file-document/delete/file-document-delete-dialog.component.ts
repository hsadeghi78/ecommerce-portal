import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFileDocument } from '../file-document.model';
import { FileDocumentService } from '../service/file-document.service';

@Component({
  standalone: true,
  templateUrl: './file-document-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FileDocumentDeleteDialogComponent {
  fileDocument?: IFileDocument;

  constructor(
    protected fileDocumentService: FileDocumentService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileDocumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
