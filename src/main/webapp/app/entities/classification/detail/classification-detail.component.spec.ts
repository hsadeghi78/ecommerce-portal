import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClassificationDetailComponent } from './classification-detail.component';

describe('Classification Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClassificationDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ClassificationDetailComponent,
              resolve: { classification: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ClassificationDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load classification on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClassificationDetailComponent);

      // THEN
      expect(instance.classification).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
