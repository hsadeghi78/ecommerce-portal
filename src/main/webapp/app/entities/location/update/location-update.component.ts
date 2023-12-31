import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IGeoDivision } from 'app/entities/geo-division/geo-division.model';
import { GeoDivisionService } from 'app/entities/geo-division/service/geo-division.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { LocationService } from '../service/location.service';
import { ILocation } from '../location.model';
import { LocationFormService, LocationFormGroup } from './location-form.service';

@Component({
  standalone: true,
  selector: 'jhi-location-update',
  templateUrl: './location-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LocationUpdateComponent implements OnInit {
  isSaving = false;
  location: ILocation | null = null;

  geoDivisionsSharedCollection: IGeoDivision[] = [];
  partiesSharedCollection: IParty[] = [];

  editForm: LocationFormGroup = this.locationFormService.createLocationFormGroup();

  constructor(
    protected locationService: LocationService,
    protected locationFormService: LocationFormService,
    protected geoDivisionService: GeoDivisionService,
    protected partyService: PartyService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareGeoDivision = (o1: IGeoDivision | null, o2: IGeoDivision | null): boolean => this.geoDivisionService.compareGeoDivision(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ location }) => {
      this.location = location;
      if (location) {
        this.updateForm(location);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const location = this.locationFormService.getLocation(this.editForm);
    if (location.id !== null) {
      this.subscribeToSaveResponse(this.locationService.update(location));
    } else {
      this.subscribeToSaveResponse(this.locationService.create(location));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocation>>): void {
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

  protected updateForm(location: ILocation): void {
    this.location = location;
    this.locationFormService.resetForm(this.editForm, location);

    this.geoDivisionsSharedCollection = this.geoDivisionService.addGeoDivisionToCollectionIfMissing<IGeoDivision>(
      this.geoDivisionsSharedCollection,
      location.geoDivision,
    );
    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing<IParty>(this.partiesSharedCollection, location.party);
  }

  protected loadRelationshipsOptions(): void {
    this.geoDivisionService
      .query()
      .pipe(map((res: HttpResponse<IGeoDivision[]>) => res.body ?? []))
      .pipe(
        map((geoDivisions: IGeoDivision[]) =>
          this.geoDivisionService.addGeoDivisionToCollectionIfMissing<IGeoDivision>(geoDivisions, this.location?.geoDivision),
        ),
      )
      .subscribe((geoDivisions: IGeoDivision[]) => (this.geoDivisionsSharedCollection = geoDivisions));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.location?.party)))
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));
  }
}
