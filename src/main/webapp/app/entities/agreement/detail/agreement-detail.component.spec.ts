import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AgreementDetailComponent } from './agreement-detail.component';

describe('Agreement Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgreementDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AgreementDetailComponent,
              resolve: { agreement: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AgreementDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load agreement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AgreementDetailComponent);

      // THEN
      expect(instance.agreement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
