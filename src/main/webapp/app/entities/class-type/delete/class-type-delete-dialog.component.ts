import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IClassType } from '../class-type.model';
import { ClassTypeService } from '../service/class-type.service';

@Component({
  standalone: true,
  templateUrl: './class-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ClassTypeDeleteDialogComponent {
  classType?: IClassType;

  constructor(
    protected classTypeService: ClassTypeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
