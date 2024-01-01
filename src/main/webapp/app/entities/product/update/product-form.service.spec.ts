import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product.test-samples';

import { ProductFormService } from './product-form.service';

describe('Product Form Service', () => {
  let service: ProductFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductFormService);
  });

  describe('Service methods', () => {
    describe('createProductFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            typeClassId: expect.any(Object),
            brandClassId: expect.any(Object),
            sizee: expect.any(Object),
            regularSizeClassId: expect.any(Object),
            languageClassId: expect.any(Object),
            description: expect.any(Object),
            keywords: expect.any(Object),
            photo1: expect.any(Object),
            nationalityClassId: expect.any(Object),
            count: expect.any(Object),
            discount: expect.any(Object),
            originalPrice: expect.any(Object),
            finalPrice: expect.any(Object),
            publishDate: expect.any(Object),
            transportDate: expect.any(Object),
            currencyClassId: expect.any(Object),
            bonus: expect.any(Object),
            warrantyClassId: expect.any(Object),
            deliveryPlaceClassId: expect.any(Object),
            paymentPlaceClassId: expect.any(Object),
            performance: expect.any(Object),
            originalityClassId: expect.any(Object),
            satisfaction: expect.any(Object),
            used: expect.any(Object),
            documents: expect.any(Object),
            category: expect.any(Object),
            party: expect.any(Object),
            parent: expect.any(Object),
            campaigns: expect.any(Object),
          }),
        );
      });

      it('passing IProduct should create a new form with FormGroup', () => {
        const formGroup = service.createProductFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            typeClassId: expect.any(Object),
            brandClassId: expect.any(Object),
            sizee: expect.any(Object),
            regularSizeClassId: expect.any(Object),
            languageClassId: expect.any(Object),
            description: expect.any(Object),
            keywords: expect.any(Object),
            photo1: expect.any(Object),
            nationalityClassId: expect.any(Object),
            count: expect.any(Object),
            discount: expect.any(Object),
            originalPrice: expect.any(Object),
            finalPrice: expect.any(Object),
            publishDate: expect.any(Object),
            transportDate: expect.any(Object),
            currencyClassId: expect.any(Object),
            bonus: expect.any(Object),
            warrantyClassId: expect.any(Object),
            deliveryPlaceClassId: expect.any(Object),
            paymentPlaceClassId: expect.any(Object),
            performance: expect.any(Object),
            originalityClassId: expect.any(Object),
            satisfaction: expect.any(Object),
            used: expect.any(Object),
            documents: expect.any(Object),
            category: expect.any(Object),
            party: expect.any(Object),
            parent: expect.any(Object),
            campaigns: expect.any(Object),
          }),
        );
      });
    });

    describe('getProduct', () => {
      it('should return NewProduct for default Product initial value', () => {
        const formGroup = service.createProductFormGroup(sampleWithNewData);

        const product = service.getProduct(formGroup) as any;

        expect(product).toMatchObject(sampleWithNewData);
      });

      it('should return NewProduct for empty Product initial value', () => {
        const formGroup = service.createProductFormGroup();

        const product = service.getProduct(formGroup) as any;

        expect(product).toMatchObject({});
      });

      it('should return IProduct', () => {
        const formGroup = service.createProductFormGroup(sampleWithRequiredData);

        const product = service.getProduct(formGroup) as any;

        expect(product).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProduct should not enable id FormControl', () => {
        const formGroup = service.createProductFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProduct should disable id FormControl', () => {
        const formGroup = service.createProductFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
