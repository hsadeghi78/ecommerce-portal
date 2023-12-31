import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFactor } from 'app/entities/factor/factor.model';
import { FactorService } from 'app/entities/factor/service/factor.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { FactorItemService } from '../service/factor-item.service';
import { IFactorItem } from '../factor-item.model';
import { FactorItemFormService, FactorItemFormGroup } from './factor-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-factor-item-update',
  templateUrl: './factor-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FactorItemUpdateComponent implements OnInit {
  isSaving = false;
  factorItem: IFactorItem | null = null;

  factorsSharedCollection: IFactor[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm: FactorItemFormGroup = this.factorItemFormService.createFactorItemFormGroup();

  constructor(
    protected factorItemService: FactorItemService,
    protected factorItemFormService: FactorItemFormService,
    protected factorService: FactorService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareFactor = (o1: IFactor | null, o2: IFactor | null): boolean => this.factorService.compareFactor(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factorItem }) => {
      this.factorItem = factorItem;
      if (factorItem) {
        this.updateForm(factorItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factorItem = this.factorItemFormService.getFactorItem(this.editForm);
    if (factorItem.id !== null) {
      this.subscribeToSaveResponse(this.factorItemService.update(factorItem));
    } else {
      this.subscribeToSaveResponse(this.factorItemService.create(factorItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactorItem>>): void {
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

  protected updateForm(factorItem: IFactorItem): void {
    this.factorItem = factorItem;
    this.factorItemFormService.resetForm(this.editForm, factorItem);

    this.factorsSharedCollection = this.factorService.addFactorToCollectionIfMissing<IFactor>(
      this.factorsSharedCollection,
      factorItem.factor,
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      factorItem.product,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.factorService
      .query()
      .pipe(map((res: HttpResponse<IFactor[]>) => res.body ?? []))
      .pipe(map((factors: IFactor[]) => this.factorService.addFactorToCollectionIfMissing<IFactor>(factors, this.factorItem?.factor)))
      .subscribe((factors: IFactor[]) => (this.factorsSharedCollection = factors));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.factorItem?.product)),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
