import { Component } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'jhi-new-step-images',
  templateUrl: './new-step-images.component.html',
  styleUrls: ['./new-step-images.component.scss'],
  standalone: true,
  imports: [MatButtonModule, MatStepperModule, FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule],
})
export class NewStepImagesComponent {
  imagesForm = this._formBuilder.group({});

  constructor(private _formBuilder: FormBuilder) {}
}
