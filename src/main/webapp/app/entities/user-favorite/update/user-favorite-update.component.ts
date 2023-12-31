import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IUserFavorite } from '../user-favorite.model';
import { UserFavoriteService } from '../service/user-favorite.service';
import { UserFavoriteFormService, UserFavoriteFormGroup } from './user-favorite-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-favorite-update',
  templateUrl: './user-favorite-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserFavoriteUpdateComponent implements OnInit {
  isSaving = false;
  userFavorite: IUserFavorite | null = null;

  productsSharedCollection: IProduct[] = [];

  editForm: UserFavoriteFormGroup = this.userFavoriteFormService.createUserFavoriteFormGroup();

  constructor(
    protected userFavoriteService: UserFavoriteService,
    protected userFavoriteFormService: UserFavoriteFormService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userFavorite }) => {
      this.userFavorite = userFavorite;
      if (userFavorite) {
        this.updateForm(userFavorite);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userFavorite = this.userFavoriteFormService.getUserFavorite(this.editForm);
    if (userFavorite.id !== null) {
      this.subscribeToSaveResponse(this.userFavoriteService.update(userFavorite));
    } else {
      this.subscribeToSaveResponse(this.userFavoriteService.create(userFavorite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserFavorite>>): void {
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

  protected updateForm(userFavorite: IUserFavorite): void {
    this.userFavorite = userFavorite;
    this.userFavoriteFormService.resetForm(this.editForm, userFavorite);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      userFavorite.product,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.userFavorite?.product)),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
