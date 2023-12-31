import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConfigDetailComponent } from './config-detail.component';

describe('Config Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfigDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ConfigDetailComponent,
              resolve: { config: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ConfigDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load config on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConfigDetailComponent);

      // THEN
      expect(instance.config).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
