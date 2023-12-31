import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserComment } from '../user-comment.model';
import { UserCommentService } from '../service/user-comment.service';

@Component({
  standalone: true,
  templateUrl: './user-comment-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserCommentDeleteDialogComponent {
  userComment?: IUserComment;

  constructor(
    protected userCommentService: UserCommentService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userCommentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
