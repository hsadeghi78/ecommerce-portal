import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProductItemDetailComponent } from './product-item-detail.component';

describe('ProductItem Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductItemDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProductItemDetailComponent,
              resolve: { productItem: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProductItemDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load productItem on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProductItemDetailComponent);

      // THEN
      expect(instance.productItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
