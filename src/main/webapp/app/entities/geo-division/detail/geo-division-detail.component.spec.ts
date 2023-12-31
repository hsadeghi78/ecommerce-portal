import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { GeoDivisionDetailComponent } from './geo-division-detail.component';

describe('GeoDivision Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GeoDivisionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: GeoDivisionDetailComponent,
              resolve: { geoDivision: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(GeoDivisionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load geoDivision on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GeoDivisionDetailComponent);

      // THEN
      expect(instance.geoDivision).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
