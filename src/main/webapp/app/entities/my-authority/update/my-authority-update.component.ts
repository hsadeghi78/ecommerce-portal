import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMyAuthority } from '../my-authority.model';
import { MyAuthorityService } from '../service/my-authority.service';
import { MyAuthorityFormService, MyAuthorityFormGroup } from './my-authority-form.service';

@Component({
  standalone: true,
  selector: 'jhi-my-authority-update',
  templateUrl: './my-authority-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MyAuthorityUpdateComponent implements OnInit {
  isSaving = false;
  myAuthority: IMyAuthority | null = null;

  myAuthoritiesSharedCollection: IMyAuthority[] = [];

  editForm: MyAuthorityFormGroup = this.myAuthorityFormService.createMyAuthorityFormGroup();

  constructor(
    protected myAuthorityService: MyAuthorityService,
    protected myAuthorityFormService: MyAuthorityFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareMyAuthority = (o1: IMyAuthority | null, o2: IMyAuthority | null): boolean => this.myAuthorityService.compareMyAuthority(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ myAuthority }) => {
      this.myAuthority = myAuthority;
      if (myAuthority) {
        this.updateForm(myAuthority);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const myAuthority = this.myAuthorityFormService.getMyAuthority(this.editForm);
    if (myAuthority.id !== null) {
      this.subscribeToSaveResponse(this.myAuthorityService.update(myAuthority));
    } else {
      this.subscribeToSaveResponse(this.myAuthorityService.create(myAuthority));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMyAuthority>>): void {
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

  protected updateForm(myAuthority: IMyAuthority): void {
    this.myAuthority = myAuthority;
    this.myAuthorityFormService.resetForm(this.editForm, myAuthority);

    this.myAuthoritiesSharedCollection = this.myAuthorityService.addMyAuthorityToCollectionIfMissing<IMyAuthority>(
      this.myAuthoritiesSharedCollection,
      myAuthority.parent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.myAuthorityService
      .query()
      .pipe(map((res: HttpResponse<IMyAuthority[]>) => res.body ?? []))
      .pipe(
        map((myAuthorities: IMyAuthority[]) =>
          this.myAuthorityService.addMyAuthorityToCollectionIfMissing<IMyAuthority>(myAuthorities, this.myAuthority?.parent),
        ),
      )
      .subscribe((myAuthorities: IMyAuthority[]) => (this.myAuthoritiesSharedCollection = myAuthorities));
  }
}
