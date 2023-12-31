import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserFavorite } from '../user-favorite.model';
import { UserFavoriteService } from '../service/user-favorite.service';

@Component({
  standalone: true,
  templateUrl: './user-favorite-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserFavoriteDeleteDialogComponent {
  userFavorite?: IUserFavorite;

  constructor(
    protected userFavoriteService: UserFavoriteService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userFavoriteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
