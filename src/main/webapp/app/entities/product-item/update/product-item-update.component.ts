import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductItem } from '../product-item.model';
import { ProductItemService } from '../service/product-item.service';
import { ProductItemFormService, ProductItemFormGroup } from './product-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-product-item-update',
  templateUrl: './product-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductItemUpdateComponent implements OnInit {
  isSaving = false;
  productItem: IProductItem | null = null;

  productsSharedCollection: IProduct[] = [];

  editForm: ProductItemFormGroup = this.productItemFormService.createProductItemFormGroup();

  constructor(
    protected productItemService: ProductItemService,
    protected productItemFormService: ProductItemFormService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productItem }) => {
      this.productItem = productItem;
      if (productItem) {
        this.updateForm(productItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productItem = this.productItemFormService.getProductItem(this.editForm);
    if (productItem.id !== null) {
      this.subscribeToSaveResponse(this.productItemService.update(productItem));
    } else {
      this.subscribeToSaveResponse(this.productItemService.create(productItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(productItem: IProductItem): void {
    this.productItem = productItem;
    this.productItemFormService.resetForm(this.editForm, productItem);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      productItem.product,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productItem?.product)),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
