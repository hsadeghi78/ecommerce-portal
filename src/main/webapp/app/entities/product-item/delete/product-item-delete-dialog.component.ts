import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProductItem } from '../product-item.model';
import { ProductItemService } from '../service/product-item.service';

@Component({
  standalone: true,
  templateUrl: './product-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProductItemDeleteDialogComponent {
  productItem?: IProductItem;

  constructor(
    protected productItemService: ProductItemService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
