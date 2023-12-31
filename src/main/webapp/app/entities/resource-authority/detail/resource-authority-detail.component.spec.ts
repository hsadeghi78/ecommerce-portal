import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ResourceAuthorityDetailComponent } from './resource-authority-detail.component';

describe('ResourceAuthority Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResourceAuthorityDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ResourceAuthorityDetailComponent,
              resolve: { resourceAuthority: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ResourceAuthorityDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load resourceAuthority on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ResourceAuthorityDetailComponent);

      // THEN
      expect(instance.resourceAuthority).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
