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
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { CampaignService } from '../service/campaign.service';
import { ICampaign } from '../campaign.model';
import { CampaignFormService, CampaignFormGroup } from './campaign-form.service';

@Component({
  standalone: true,
  selector: 'jhi-campaign-update',
  templateUrl: './campaign-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CampaignUpdateComponent implements OnInit {
  isSaving = false;
  campaign: ICampaign | null = null;

  productsSharedCollection: IProduct[] = [];

  editForm: CampaignFormGroup = this.campaignFormService.createCampaignFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected campaignService: CampaignService,
    protected campaignFormService: CampaignFormService,
    protected productService: ProductService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ campaign }) => {
      this.campaign = campaign;
      if (campaign) {
        this.updateForm(campaign);
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
    const campaign = this.campaignFormService.getCampaign(this.editForm);
    if (campaign.id !== null) {
      this.subscribeToSaveResponse(this.campaignService.update(campaign));
    } else {
      this.subscribeToSaveResponse(this.campaignService.create(campaign));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICampaign>>): void {
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

  protected updateForm(campaign: ICampaign): void {
    this.campaign = campaign;
    this.campaignFormService.resetForm(this.editForm, campaign);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      ...(campaign.products ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, ...(this.campaign?.products ?? [])),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
