import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IResource } from 'app/entities/resource/resource.model';
import { ResourceService } from 'app/entities/resource/service/resource.service';
import { IMyAuthority } from 'app/entities/my-authority/my-authority.model';
import { MyAuthorityService } from 'app/entities/my-authority/service/my-authority.service';
import { Verb } from 'app/entities/enumerations/verb.model';
import { ResourceAuthorityService } from '../service/resource-authority.service';
import { IResourceAuthority } from '../resource-authority.model';
import { ResourceAuthorityFormService, ResourceAuthorityFormGroup } from './resource-authority-form.service';

@Component({
  standalone: true,
  selector: 'jhi-resource-authority-update',
  templateUrl: './resource-authority-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ResourceAuthorityUpdateComponent implements OnInit {
  isSaving = false;
  resourceAuthority: IResourceAuthority | null = null;
  verbValues = Object.keys(Verb);

  resourcesSharedCollection: IResource[] = [];
  myAuthoritiesSharedCollection: IMyAuthority[] = [];

  editForm: ResourceAuthorityFormGroup = this.resourceAuthorityFormService.createResourceAuthorityFormGroup();

  constructor(
    protected resourceAuthorityService: ResourceAuthorityService,
    protected resourceAuthorityFormService: ResourceAuthorityFormService,
    protected resourceService: ResourceService,
    protected myAuthorityService: MyAuthorityService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareResource = (o1: IResource | null, o2: IResource | null): boolean => this.resourceService.compareResource(o1, o2);

  compareMyAuthority = (o1: IMyAuthority | null, o2: IMyAuthority | null): boolean => this.myAuthorityService.compareMyAuthority(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resourceAuthority }) => {
      this.resourceAuthority = resourceAuthority;
      if (resourceAuthority) {
        this.updateForm(resourceAuthority);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resourceAuthority = this.resourceAuthorityFormService.getResourceAuthority(this.editForm);
    if (resourceAuthority.id !== null) {
      this.subscribeToSaveResponse(this.resourceAuthorityService.update(resourceAuthority));
    } else {
      this.subscribeToSaveResponse(this.resourceAuthorityService.create(resourceAuthority));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResourceAuthority>>): void {
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

  protected updateForm(resourceAuthority: IResourceAuthority): void {
    this.resourceAuthority = resourceAuthority;
    this.resourceAuthorityFormService.resetForm(this.editForm, resourceAuthority);

    this.resourcesSharedCollection = this.resourceService.addResourceToCollectionIfMissing<IResource>(
      this.resourcesSharedCollection,
      resourceAuthority.resource,
    );
    this.myAuthoritiesSharedCollection = this.myAuthorityService.addMyAuthorityToCollectionIfMissing<IMyAuthority>(
      this.myAuthoritiesSharedCollection,
      resourceAuthority.myAuthority,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.resourceService
      .query()
      .pipe(map((res: HttpResponse<IResource[]>) => res.body ?? []))
      .pipe(
        map((resources: IResource[]) =>
          this.resourceService.addResourceToCollectionIfMissing<IResource>(resources, this.resourceAuthority?.resource),
        ),
      )
      .subscribe((resources: IResource[]) => (this.resourcesSharedCollection = resources));

    this.myAuthorityService
      .query()
      .pipe(map((res: HttpResponse<IMyAuthority[]>) => res.body ?? []))
      .pipe(
        map((myAuthorities: IMyAuthority[]) =>
          this.myAuthorityService.addMyAuthorityToCollectionIfMissing<IMyAuthority>(myAuthorities, this.resourceAuthority?.myAuthority),
        ),
      )
      .subscribe((myAuthorities: IMyAuthority[]) => (this.myAuthoritiesSharedCollection = myAuthorities));
  }
}
