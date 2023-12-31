import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserCommentDetailComponent } from './user-comment-detail.component';

describe('UserComment Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserCommentDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UserCommentDetailComponent,
              resolve: { userComment: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UserCommentDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load userComment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserCommentDetailComponent);

      // THEN
      expect(instance.userComment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
