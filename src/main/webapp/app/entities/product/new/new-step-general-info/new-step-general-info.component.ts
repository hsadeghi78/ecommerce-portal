import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { IProduct } from '../../product.model';
import { NgbTooltip } from '@ng-bootstrap/ng-bootstrap';
import SharedModule from '../../../../shared/shared.module';
import { translate } from '@angular/localize/tools';
import { Performance } from '../../../enumerations/performance.model';
import { MatSelectModule } from '@angular/material/select';
import { IVwClassification } from '../../../classification/classification.model';
import { ClassificationService } from '../../../classification/service/classification.service';
import { HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { ClassTypes } from '../../../../shared/constants/class-types.model';
import { MatCheckboxModule } from '@angular/material/checkbox';

@Component({
  selector: 'jhi-new-step-general-info',
  templateUrl: './new-step-general-info.component.html',
  styleUrls: ['./new-step-general-info.component.scss'],
  standalone: true,
  imports: [
    MatButtonModule,
    MatStepperModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    NgbTooltip,
    SharedModule,
    MatSelectModule,
    MatCheckboxModule,
  ],
})
export class NewStepGeneralInfoComponent implements OnInit {
  classificationSharedCollection: IVwClassification[] = [];
  classProductType: IVwClassification[] = [];
  classBrand: IVwClassification[] = [];
  classLanguage: IVwClassification[] = [];
  classNationality: IVwClassification[] = [];
  classWarranty: IVwClassification[] = [];
  classDelivery: IVwClassification[] = [];
  classOrginality: IVwClassification[] = [];
  classRegSize: IVwClassification[] = [];

  generalInfoForm = this._formBuilder.group({
    name: new FormControl('', {
      validators: [Validators.required, Validators.maxLength(500)],
    }),
    brandClassId: new FormControl(''),
    sizee: new FormControl('', {
      validators: [Validators.maxLength(100)],
    }),
    regularSizeClassId: new FormControl('', {
      validators: [Validators.required],
    }),
    languageClassId: new FormControl('', {
      validators: [Validators.required],
    }),
    description: new FormControl('', {
      validators: [Validators.maxLength(2500)],
    }),
    keywords: new FormControl('', {
      validators: [Validators.maxLength(500)],
    }),
    nationalityClassId: new FormControl(null),
    count: new FormControl(null, {
      validators: [Validators.required],
    }),
    publishDate: new FormControl(null, {
      validators: [Validators.required],
    }),
    transportDate: new FormControl(null, {
      validators: [Validators.required],
    }),
    warrantyClassId: new FormControl(null),
    deliveryPlaceClassId: new FormControl(null),
    category: new FormControl(null, {
      validators: [Validators.required],
    }),
    photo1: new FormControl(null, {
      validators: [Validators.required],
    }),
    photo1ContentType: new FormControl(null),
    performance: new FormControl(performance),
    originalityClassId: new FormControl(null),
    satisfaction: new FormControl(null),
    used: new FormControl(null, {
      validators: [Validators.required],
    }),
  });

  performanceValues = Object.keys(Performance);
  constructor(
    private _formBuilder: FormBuilder,
    private classificationService: ClassificationService,
  ) {}

  ngOnInit(): void {
    this.loadRelationshipsOptions();
  }

  private loadRelationshipsOptions() {
    this.classificationService
      .queryView({ page: 0, size: 2000 })
      .pipe(map((res: HttpResponse<IVwClassification[]>) => res.body ?? []))
      .subscribe((classes: IVwClassification[]) => {
        this.classificationSharedCollection = classes;
        debugger;
        this.classBrand = this.classificationSharedCollection.filter(cls => cls.typeCode === ClassTypes.BRAND);
        this.classProductType = this.classificationSharedCollection.filter(cls => cls.typeCode === ClassTypes.PRODUCT_TYPE);
        this.classLanguage = this.classificationSharedCollection.filter(cls => cls.typeCode === ClassTypes.LANGUAGE);
        this.classRegSize = this.classificationSharedCollection.filter(cls => cls.typeCode === ClassTypes.REGULAR_SIZE);
        this.classNationality = this.classificationSharedCollection.filter(cls => cls.typeCode === ClassTypes.NATIONALITY);
        this.classWarranty = this.classificationSharedCollection.filter(cls => cls.typeCode === ClassTypes.WARRANTY);
        this.classDelivery = this.classificationSharedCollection.filter(cls => cls.typeCode === ClassTypes.DELIVERY_PLACE);
        this.classOrginality = this.classificationSharedCollection.filter(cls => cls.typeCode === ClassTypes.ORIGINALITY);
      });
  }
}
