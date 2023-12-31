import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IFileDocument } from 'app/entities/file-document/file-document.model';
import { FileDocumentService } from 'app/entities/file-document/service/file-document.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { Performance } from 'app/entities/enumerations/performance.model';
import { ProductService } from '../service/product.service';
import { IProduct } from '../product.model';
import { ProductFormService, ProductFormGroup } from './product-form.service';

@Component({
  standalone: true,
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  product: IProduct | null = null;
  performanceValues = Object.keys(Performance);

  productsSharedCollection: IProduct[] = [];
  fileDocumentsSharedCollection: IFileDocument[] = [];
  categoriesSharedCollection: ICategory[] = [];
  partiesSharedCollection: IParty[] = [];

  editForm: ProductFormGroup = this.productFormService.createProductFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected productService: ProductService,
    protected productFormService: ProductFormService,
    protected fileDocumentService: FileDocumentService,
    protected categoryService: CategoryService,
    protected partyService: PartyService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareFileDocument = (o1: IFileDocument | null, o2: IFileDocument | null): boolean =>
    this.fileDocumentService.compareFileDocument(o1, o2);

  compareCategory = (o1: ICategory | null, o2: ICategory | null): boolean => this.categoryService.compareCategory(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (product) {
        this.updateForm(product);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('ecommercePortalApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.productFormService.getProduct(this.editForm);
    if (product.id !== null) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.product = product;
    this.productFormService.resetForm(this.editForm, product);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      product.parent,
    );
    this.fileDocumentsSharedCollection = this.fileDocumentService.addFileDocumentToCollectionIfMissing<IFileDocument>(
      this.fileDocumentsSharedCollection,
      ...(product.documents ?? []),
    );
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing<ICategory>(
      this.categoriesSharedCollection,
      product.category,
    );
    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing<IParty>(this.partiesSharedCollection, product.party);
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.product?.parent)))
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.fileDocumentService
      .query()
      .pipe(map((res: HttpResponse<IFileDocument[]>) => res.body ?? []))
      .pipe(
        map((fileDocuments: IFileDocument[]) =>
          this.fileDocumentService.addFileDocumentToCollectionIfMissing<IFileDocument>(fileDocuments, ...(this.product?.documents ?? [])),
        ),
      )
      .subscribe((fileDocuments: IFileDocument[]) => (this.fileDocumentsSharedCollection = fileDocuments));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing<ICategory>(categories, this.product?.category),
        ),
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.product?.party)))
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));
  }
}
