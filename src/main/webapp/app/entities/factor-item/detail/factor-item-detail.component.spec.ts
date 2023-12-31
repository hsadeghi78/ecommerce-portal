import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FactorItemDetailComponent } from './factor-item-detail.component';

describe('FactorItem Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FactorItemDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FactorItemDetailComponent,
              resolve: { factorItem: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FactorItemDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load factorItem on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FactorItemDetailComponent);

      // THEN
      expect(instance.factorItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
