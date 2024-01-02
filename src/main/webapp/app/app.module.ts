import { NgModule, LOCALE_ID, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import locale from '@angular/common/locales/en';
import { BrowserModule, Title } from '@angular/platform-browser';
import { TitleStrategy } from '@angular/router';
import { ServiceWorkerModule } from '@angular/service-worker';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import dayjs from 'dayjs/esm';
import { NgbDateAdapter, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import './config/dayjs';
import { TranslationModule } from 'app/shared/language/translation.module';
import { httpInterceptorProviders } from 'app/core/interceptor/index';
import FindLanguageFromKeyPipe from 'app/shared/language/find-language-from-key.pipe';
import { AppRoutingModule } from './app-routing.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { BrowserAnimationsModule, provideAnimations } from '@angular/platform-browser/animations';
import { NgbDateDayjsAdapter } from './config/datepicker-adapter';
import { fontAwesomeIcons } from './config/font-awesome-icons';
import MainComponent from './layouts/main/main.component';
import MainModule from './layouts/main/main.module';
import { AppPageTitleStrategy } from './app-page-title-strategy';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSliderModule } from '@angular/material/slider';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NewProductStepperComponent } from './entities/product/new/new-product-stepper/new-product-stepper.component';
import { NewStepGeneralInfoComponent } from './entities/product/new/new-step-general-info/new-step-general-info.component';
import { NewStepFinanceInfoComponent } from './entities/product/new/new-step-finance-info/new-step-finance-info.component';
import { NewStepExtraInfoComponent } from './entities/product/new/new-step-extra-info/new-step-extra-info.component';
import { NewStepImagesComponent } from './entities/product/new/new-step-images/new-step-images.component';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    AppRoutingModule,
    // Set this to true to enable service worker (PWA)
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
    HttpClientModule,
    MainModule,
    TranslationModule,
    FormsModule,
    ReactiveFormsModule,
    MatSlideToggleModule,
    MatSliderModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatStepperModule,
  ],
  providers: [
    Title,
    { provide: LOCALE_ID, useValue: 'en' },
    { provide: NgbDateAdapter, useClass: NgbDateDayjsAdapter },
    FindLanguageFromKeyPipe,
    httpInterceptorProviders,
    { provide: TitleStrategy, useClass: AppPageTitleStrategy },
    provideAnimations(),
  ],
  bootstrap: [MainComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {
  constructor(applicationConfigService: ApplicationConfigService, iconLibrary: FaIconLibrary, dpConfig: NgbDatepickerConfig) {
    applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    iconLibrary.addIcons(...fontAwesomeIcons);
    dpConfig.minDate = { year: dayjs().subtract(100, 'year').year(), month: 1, day: 1 };
  }
}
