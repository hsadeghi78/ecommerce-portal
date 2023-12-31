import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClassTypeDetailComponent } from './class-type-detail.component';

describe('ClassType Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClassTypeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ClassTypeDetailComponent,
              resolve: { classType: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ClassTypeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load classType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClassTypeDetailComponent);

      // THEN
      expect(instance.classType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
