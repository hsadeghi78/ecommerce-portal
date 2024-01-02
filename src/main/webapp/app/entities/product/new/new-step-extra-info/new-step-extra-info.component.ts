import { Component } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'jhi-new-step-extra-info',
  templateUrl: './new-step-extra-info.component.html',
  styleUrls: ['./new-step-extra-info.component.scss'],
  standalone: true,
  imports: [MatButtonModule, MatStepperModule, FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule],
})
export class NewStepExtraInfoComponent {
  extraInfoForm = this._formBuilder.group({});

  constructor(private _formBuilder: FormBuilder) {}
}
