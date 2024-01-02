import { Component, Input, ViewChild } from '@angular/core';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { NewStepGeneralInfoComponent } from '../new-step-general-info/new-step-general-info.component';
import { NewStepExtraInfoComponent } from '../new-step-extra-info/new-step-extra-info.component';
import { NewStepFinanceInfoComponent } from '../new-step-finance-info/new-step-finance-info.component';
import { NewStepImagesComponent } from '../new-step-images/new-step-images.component';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'jhi-new-product-stepper',
  templateUrl: './new-product-stepper.component.html',
  styleUrls: ['./new-product-stepper.component.scss'],
  standalone: true,
  imports: [
    MatButtonModule,
    MatStepperModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,

    NewStepGeneralInfoComponent,
    NewStepExtraInfoComponent,
    NewStepFinanceInfoComponent,
    NewStepImagesComponent,
  ],
})
export class NewProductStepperComponent {
  @Input() customerId?: number | undefined;
  @ViewChild('stepper', { static: true }) stepper: MatStepper | any;
  @ViewChild('stepNewProdGeneralInfo', { static: true }) stepNewProdGeneralInfo: NewStepGeneralInfoComponent | any;
  @ViewChild('stepNewProdExtraInfo', { static: true }) stepNewProdExtraInfo: NewStepExtraInfoComponent | any;
  @ViewChild('stepNewProdFincInfo', { static: true }) stepNewProdFincInfo: NewStepFinanceInfoComponent | any;
  @ViewChild('stepNewProdImages', { static: true }) stepNewProdImages: NewStepImagesComponent | any;

  get stepNewProdGeneralInfoForm(): any {
    return this.stepNewProdGeneralInfo.generalInfoForm;
  }

  get stepNewProdExtraInfoForm(): any {
    return this.stepNewProdExtraInfo.extraInfoForm;
  }

  get stepNewProdFincInfoForm(): any {
    return this.stepNewProdFincInfo.finInfoForm;
  }

  get stepNewProdImagesForm(): any {
    return this.stepNewProdImages.imagesForm;
  }
}
