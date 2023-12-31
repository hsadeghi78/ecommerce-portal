import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IGeoDivision } from '../geo-division.model';
import { GeoDivisionService } from '../service/geo-division.service';

@Component({
  standalone: true,
  templateUrl: './geo-division-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class GeoDivisionDeleteDialogComponent {
  geoDivision?: IGeoDivision;

  constructor(
    protected geoDivisionService: GeoDivisionService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.geoDivisionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
