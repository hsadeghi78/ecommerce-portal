import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IConsumeMaterial } from '../consume-material.model';
import { ConsumeMaterialService } from '../service/consume-material.service';
import { ConsumeMaterialFormService, ConsumeMaterialFormGroup } from './consume-material-form.service';

@Component({
  standalone: true,
  selector: 'jhi-consume-material-update',
  templateUrl: './consume-material-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConsumeMaterialUpdateComponent implements OnInit {
  isSaving = false;
  consumeMaterial: IConsumeMaterial | null = null;

  productsSharedCollection: IProduct[] = [];

  editForm: ConsumeMaterialFormGroup = this.consumeMaterialFormService.createConsumeMaterialFormGroup();

  constructor(
    protected consumeMaterialService: ConsumeMaterialService,
    protected consumeMaterialFormService: ConsumeMaterialFormService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consumeMaterial }) => {
      this.consumeMaterial = consumeMaterial;
      if (consumeMaterial) {
        this.updateForm(consumeMaterial);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consumeMaterial = this.consumeMaterialFormService.getConsumeMaterial(this.editForm);
    if (consumeMaterial.id !== null) {
      this.subscribeToSaveResponse(this.consumeMaterialService.update(consumeMaterial));
    } else {
      this.subscribeToSaveResponse(this.consumeMaterialService.create(consumeMaterial));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsumeMaterial>>): void {
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

  protected updateForm(consumeMaterial: IConsumeMaterial): void {
    this.consumeMaterial = consumeMaterial;
    this.consumeMaterialFormService.resetForm(this.editForm, consumeMaterial);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      consumeMaterial.product,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, this.consumeMaterial?.product),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
