import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICriticism } from '../criticism.model';
import { CriticismService } from '../service/criticism.service';
import { CriticismFormService, CriticismFormGroup } from './criticism-form.service';

@Component({
  standalone: true,
  selector: 'jhi-criticism-update',
  templateUrl: './criticism-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CriticismUpdateComponent implements OnInit {
  isSaving = false;
  criticism: ICriticism | null = null;

  editForm: CriticismFormGroup = this.criticismFormService.createCriticismFormGroup();

  constructor(
    protected criticismService: CriticismService,
    protected criticismFormService: CriticismFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ criticism }) => {
      this.criticism = criticism;
      if (criticism) {
        this.updateForm(criticism);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const criticism = this.criticismFormService.getCriticism(this.editForm);
    if (criticism.id !== null) {
      this.subscribeToSaveResponse(this.criticismService.update(criticism));
    } else {
      this.subscribeToSaveResponse(this.criticismService.create(criticism));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICriticism>>): void {
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

  protected updateForm(criticism: ICriticism): void {
    this.criticism = criticism;
    this.criticismFormService.resetForm(this.editForm, criticism);
  }
}
