import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { FileDocumentService } from '../service/file-document.service';
import { IFileDocument } from '../file-document.model';
import { FileDocumentFormService, FileDocumentFormGroup } from './file-document-form.service';

@Component({
  standalone: true,
  selector: 'jhi-file-document-update',
  templateUrl: './file-document-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FileDocumentUpdateComponent implements OnInit {
  isSaving = false;
  fileDocument: IFileDocument | null = null;

  editForm: FileDocumentFormGroup = this.fileDocumentFormService.createFileDocumentFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected fileDocumentService: FileDocumentService,
    protected fileDocumentFormService: FileDocumentFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileDocument }) => {
      this.fileDocument = fileDocument;
      if (fileDocument) {
        this.updateForm(fileDocument);
      }
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fileDocument = this.fileDocumentFormService.getFileDocument(this.editForm);
    if (fileDocument.id !== null) {
      this.subscribeToSaveResponse(this.fileDocumentService.update(fileDocument));
    } else {
      this.subscribeToSaveResponse(this.fileDocumentService.create(fileDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileDocument>>): void {
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

  protected updateForm(fileDocument: IFileDocument): void {
    this.fileDocument = fileDocument;
    this.fileDocumentFormService.resetForm(this.editForm, fileDocument);
  }
}
