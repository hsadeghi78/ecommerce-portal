import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'jhi-new-step-finance-info',
  templateUrl: './new-step-finance-info.component.html',
  styleUrls: ['./new-step-finance-info.component.scss'],
  standalone: true,
  imports: [MatButtonModule, MatStepperModule, FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule],
})
export class NewStepFinanceInfoComponent {
  finInfoForm = this._formBuilder.group({
    discount: new FormControl(null),
    originalPrice: new FormControl(null, {
      validators: [Validators.required],
    }),
    finalPrice: new FormControl(null, {
      validators: [Validators.required],
    }),
    currencyClassId: new FormControl(null, {
      validators: [Validators.required],
    }),
    bonus: new FormControl(null),
    paymentPlaceClassId: new FormControl(null),

    documents: new FormControl(null ?? []),
    campaigns: new FormControl(null ?? []),
  });

  constructor(private _formBuilder: FormBuilder) {}
}
