import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { IProduct } from '../../product.model';

@Component({
  selector: 'jhi-new-step-general-info',
  templateUrl: './new-step-general-info.component.html',
  styleUrls: ['./new-step-general-info.component.scss'],
  standalone: true,
  imports: [MatButtonModule, MatStepperModule, FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule],
})
export class NewStepGeneralInfoComponent {
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
  constructor(private _formBuilder: FormBuilder) {}
}
