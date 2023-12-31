import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IFactor } from 'app/entities/factor/factor.model';
import { FactorService } from 'app/entities/factor/service/factor.service';
import { UserCommentService } from '../service/user-comment.service';
import { IUserComment } from '../user-comment.model';
import { UserCommentFormService, UserCommentFormGroup } from './user-comment-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-comment-update',
  templateUrl: './user-comment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserCommentUpdateComponent implements OnInit {
  isSaving = false;
  userComment: IUserComment | null = null;

  userCommentsSharedCollection: IUserComment[] = [];
  partiesSharedCollection: IParty[] = [];
  productsSharedCollection: IProduct[] = [];
  factorsSharedCollection: IFactor[] = [];

  editForm: UserCommentFormGroup = this.userCommentFormService.createUserCommentFormGroup();

  constructor(
    protected userCommentService: UserCommentService,
    protected userCommentFormService: UserCommentFormService,
    protected partyService: PartyService,
    protected productService: ProductService,
    protected factorService: FactorService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUserComment = (o1: IUserComment | null, o2: IUserComment | null): boolean => this.userCommentService.compareUserComment(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareFactor = (o1: IFactor | null, o2: IFactor | null): boolean => this.factorService.compareFactor(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userComment }) => {
      this.userComment = userComment;
      if (userComment) {
        this.updateForm(userComment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userComment = this.userCommentFormService.getUserComment(this.editForm);
    if (userComment.id !== null) {
      this.subscribeToSaveResponse(this.userCommentService.update(userComment));
    } else {
      this.subscribeToSaveResponse(this.userCommentService.create(userComment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserComment>>): void {
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

  protected updateForm(userComment: IUserComment): void {
    this.userComment = userComment;
    this.userCommentFormService.resetForm(this.editForm, userComment);

    this.userCommentsSharedCollection = this.userCommentService.addUserCommentToCollectionIfMissing<IUserComment>(
      this.userCommentsSharedCollection,
      userComment.parent,
    );
    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing<IParty>(this.partiesSharedCollection, userComment.party);
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      userComment.product,
    );
    this.factorsSharedCollection = this.factorService.addFactorToCollectionIfMissing<IFactor>(
      this.factorsSharedCollection,
      userComment.factor,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userCommentService
      .query()
      .pipe(map((res: HttpResponse<IUserComment[]>) => res.body ?? []))
      .pipe(
        map((userComments: IUserComment[]) =>
          this.userCommentService.addUserCommentToCollectionIfMissing<IUserComment>(userComments, this.userComment?.parent),
        ),
      )
      .subscribe((userComments: IUserComment[]) => (this.userCommentsSharedCollection = userComments));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.userComment?.party)))
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.userComment?.product)),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.factorService
      .query()
      .pipe(map((res: HttpResponse<IFactor[]>) => res.body ?? []))
      .pipe(map((factors: IFactor[]) => this.factorService.addFactorToCollectionIfMissing<IFactor>(factors, this.userComment?.factor)))
      .subscribe((factors: IFactor[]) => (this.factorsSharedCollection = factors));
  }
}
