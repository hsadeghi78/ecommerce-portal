import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConsumeMaterialDetailComponent } from './consume-material-detail.component';

describe('ConsumeMaterial Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsumeMaterialDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ConsumeMaterialDetailComponent,
              resolve: { consumeMaterial: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ConsumeMaterialDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load consumeMaterial on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConsumeMaterialDetailComponent);

      // THEN
      expect(instance.consumeMaterial).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
