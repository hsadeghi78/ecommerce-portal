import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { FactorService } from '../service/factor.service';
import { IFactor } from '../factor.model';
import { FactorFormService, FactorFormGroup } from './factor-form.service';

@Component({
  standalone: true,
  selector: 'jhi-factor-update',
  templateUrl: './factor-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FactorUpdateComponent implements OnInit {
  isSaving = false;
  factor: IFactor | null = null;

  locationsCollection: ILocation[] = [];
  partiesSharedCollection: IParty[] = [];

  editForm: FactorFormGroup = this.factorFormService.createFactorFormGroup();

  constructor(
    protected factorService: FactorService,
    protected factorFormService: FactorFormService,
    protected locationService: LocationService,
    protected partyService: PartyService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareLocation = (o1: ILocation | null, o2: ILocation | null): boolean => this.locationService.compareLocation(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factor }) => {
      this.factor = factor;
      if (factor) {
        this.updateForm(factor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factor = this.factorFormService.getFactor(this.editForm);
    if (factor.id !== null) {
      this.subscribeToSaveResponse(this.factorService.update(factor));
    } else {
      this.subscribeToSaveResponse(this.factorService.create(factor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactor>>): void {
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

  protected updateForm(factor: IFactor): void {
    this.factor = factor;
    this.factorFormService.resetForm(this.editForm, factor);

    this.locationsCollection = this.locationService.addLocationToCollectionIfMissing<ILocation>(this.locationsCollection, factor.location);
    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing<IParty>(
      this.partiesSharedCollection,
      factor.buyerParty,
      factor.sellerParty,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query({ 'factorId.specified': 'false' })
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) => this.locationService.addLocationToCollectionIfMissing<ILocation>(locations, this.factor?.location)),
      )
      .subscribe((locations: ILocation[]) => (this.locationsCollection = locations));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.factor?.buyerParty, this.factor?.sellerParty),
        ),
      )
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));
  }
}
