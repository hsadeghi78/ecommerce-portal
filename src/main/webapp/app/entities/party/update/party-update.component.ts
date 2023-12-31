import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { PartyService } from '../service/party.service';
import { IParty } from '../party.model';
import { PartyFormService, PartyFormGroup } from './party-form.service';

@Component({
  standalone: true,
  selector: 'jhi-party-update',
  templateUrl: './party-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PartyUpdateComponent implements OnInit {
  isSaving = false;
  party: IParty | null = null;

  editForm: PartyFormGroup = this.partyFormService.createPartyFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected partyService: PartyService,
    protected partyFormService: PartyFormService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ party }) => {
      this.party = party;
      if (party) {
        this.updateForm(party);
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
    const party = this.partyFormService.getParty(this.editForm);
    if (party.id !== null) {
      this.subscribeToSaveResponse(this.partyService.update(party));
    } else {
      this.subscribeToSaveResponse(this.partyService.create(party));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParty>>): void {
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

  protected updateForm(party: IParty): void {
    this.party = party;
    this.partyFormService.resetForm(this.editForm, party);
  }
}
