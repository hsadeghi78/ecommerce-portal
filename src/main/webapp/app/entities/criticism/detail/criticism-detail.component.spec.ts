import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CriticismDetailComponent } from './criticism-detail.component';

describe('Criticism Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CriticismDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CriticismDetailComponent,
              resolve: { criticism: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CriticismDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load criticism on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CriticismDetailComponent);

      // THEN
      expect(instance.criticism).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
