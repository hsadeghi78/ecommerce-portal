import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IGeoDivision } from '../geo-division.model';
import { GeoDivisionService } from '../service/geo-division.service';
import { GeoDivisionFormService, GeoDivisionFormGroup } from './geo-division-form.service';

@Component({
  standalone: true,
  selector: 'jhi-geo-division-update',
  templateUrl: './geo-division-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GeoDivisionUpdateComponent implements OnInit {
  isSaving = false;
  geoDivision: IGeoDivision | null = null;

  geoDivisionsSharedCollection: IGeoDivision[] = [];

  editForm: GeoDivisionFormGroup = this.geoDivisionFormService.createGeoDivisionFormGroup();

  constructor(
    protected geoDivisionService: GeoDivisionService,
    protected geoDivisionFormService: GeoDivisionFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareGeoDivision = (o1: IGeoDivision | null, o2: IGeoDivision | null): boolean => this.geoDivisionService.compareGeoDivision(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ geoDivision }) => {
      this.geoDivision = geoDivision;
      if (geoDivision) {
        this.updateForm(geoDivision);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const geoDivision = this.geoDivisionFormService.getGeoDivision(this.editForm);
    if (geoDivision.id !== null) {
      this.subscribeToSaveResponse(this.geoDivisionService.update(geoDivision));
    } else {
      this.subscribeToSaveResponse(this.geoDivisionService.create(geoDivision));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGeoDivision>>): void {
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

  protected updateForm(geoDivision: IGeoDivision): void {
    this.geoDivision = geoDivision;
    this.geoDivisionFormService.resetForm(this.editForm, geoDivision);

    this.geoDivisionsSharedCollection = this.geoDivisionService.addGeoDivisionToCollectionIfMissing<IGeoDivision>(
      this.geoDivisionsSharedCollection,
      geoDivision.parent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.geoDivisionService
      .query()
      .pipe(map((res: HttpResponse<IGeoDivision[]>) => res.body ?? []))
      .pipe(
        map((geoDivisions: IGeoDivision[]) =>
          this.geoDivisionService.addGeoDivisionToCollectionIfMissing<IGeoDivision>(geoDivisions, this.geoDivision?.parent),
        ),
      )
      .subscribe((geoDivisions: IGeoDivision[]) => (this.geoDivisionsSharedCollection = geoDivisions));
  }
}
