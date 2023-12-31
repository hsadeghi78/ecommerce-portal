import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserFavoriteDetailComponent } from './user-favorite-detail.component';

describe('UserFavorite Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserFavoriteDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UserFavoriteDetailComponent,
              resolve: { userFavorite: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UserFavoriteDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load userFavorite on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserFavoriteDetailComponent);

      // THEN
      expect(instance.userFavorite).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
