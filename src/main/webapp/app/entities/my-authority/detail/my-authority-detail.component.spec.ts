import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MyAuthorityDetailComponent } from './my-authority-detail.component';

describe('MyAuthority Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyAuthorityDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MyAuthorityDetailComponent,
              resolve: { myAuthority: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MyAuthorityDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load myAuthority on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MyAuthorityDetailComponent);

      // THEN
      expect(instance.myAuthority).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
