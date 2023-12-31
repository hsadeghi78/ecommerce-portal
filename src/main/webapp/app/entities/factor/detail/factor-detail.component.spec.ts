import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FactorDetailComponent } from './factor-detail.component';

describe('Factor Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FactorDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FactorDetailComponent,
              resolve: { factor: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FactorDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load factor on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FactorDetailComponent);

      // THEN
      expect(instance.factor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
