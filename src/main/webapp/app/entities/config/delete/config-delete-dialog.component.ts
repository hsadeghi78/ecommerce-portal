import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IConfig } from '../config.model';
import { ConfigService } from '../service/config.service';

@Component({
  standalone: true,
  templateUrl: './config-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConfigDeleteDialogComponent {
  config?: IConfig;

  constructor(
    protected configService: ConfigService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.configService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
