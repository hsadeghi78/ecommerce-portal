import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClassType } from 'app/entities/class-type/class-type.model';
import { ClassTypeService } from 'app/entities/class-type/service/class-type.service';
import { IClassification } from '../classification.model';
import { ClassificationService } from '../service/classification.service';
import { ClassificationFormService, ClassificationFormGroup } from './classification-form.service';

@Component({
  standalone: true,
  selector: 'jhi-classification-update',
  templateUrl: './classification-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClassificationUpdateComponent implements OnInit {
  isSaving = false;
  classification: IClassification | null = null;

  classTypesSharedCollection: IClassType[] = [];

  editForm: ClassificationFormGroup = this.classificationFormService.createClassificationFormGroup();

  constructor(
    protected classificationService: ClassificationService,
    protected classificationFormService: ClassificationFormService,
    protected classTypeService: ClassTypeService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareClassType = (o1: IClassType | null, o2: IClassType | null): boolean => this.classTypeService.compareClassType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classification }) => {
      this.classification = classification;
      if (classification) {
        this.updateForm(classification);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classification = this.classificationFormService.getClassification(this.editForm);
    if (classification.id !== null) {
      this.subscribeToSaveResponse(this.classificationService.update(classification));
    } else {
      this.subscribeToSaveResponse(this.classificationService.create(classification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassification>>): void {
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

  protected updateForm(classification: IClassification): void {
    this.classification = classification;
    this.classificationFormService.resetForm(this.editForm, classification);

    this.classTypesSharedCollection = this.classTypeService.addClassTypeToCollectionIfMissing<IClassType>(
      this.classTypesSharedCollection,
      classification.classType,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classTypeService
      .query()
      .pipe(map((res: HttpResponse<IClassType[]>) => res.body ?? []))
      .pipe(
        map((classTypes: IClassType[]) =>
          this.classTypeService.addClassTypeToCollectionIfMissing<IClassType>(classTypes, this.classification?.classType),
        ),
      )
      .subscribe((classTypes: IClassType[]) => (this.classTypesSharedCollection = classTypes));
  }
}
