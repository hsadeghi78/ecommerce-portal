import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IAgreement } from '../agreement.model';
import { AgreementService } from '../service/agreement.service';
import { AgreementFormService, AgreementFormGroup } from './agreement-form.service';

@Component({
  standalone: true,
  selector: 'jhi-agreement-update',
  templateUrl: './agreement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AgreementUpdateComponent implements OnInit {
  isSaving = false;
  agreement: IAgreement | null = null;

  partiesSharedCollection: IParty[] = [];

  editForm: AgreementFormGroup = this.agreementFormService.createAgreementFormGroup();

  constructor(
    protected agreementService: AgreementService,
    protected agreementFormService: AgreementFormService,
    protected partyService: PartyService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agreement }) => {
      this.agreement = agreement;
      if (agreement) {
        this.updateForm(agreement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agreement = this.agreementFormService.getAgreement(this.editForm);
    if (agreement.id !== null) {
      this.subscribeToSaveResponse(this.agreementService.update(agreement));
    } else {
      this.subscribeToSaveResponse(this.agreementService.create(agreement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgreement>>): void {
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

  protected updateForm(agreement: IAgreement): void {
    this.agreement = agreement;
    this.agreementFormService.resetForm(this.editForm, agreement);

    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing<IParty>(
      this.partiesSharedCollection,
      agreement.provider,
      agreement.consumer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.agreement?.provider, this.agreement?.consumer),
        ),
      )
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));
  }
}
