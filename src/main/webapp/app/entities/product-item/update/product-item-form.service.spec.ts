import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product-item.test-samples';

import { ProductItemFormService } from './product-item-form.service';

describe('ProductItem Form Service', () => {
  let service: ProductItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductItemFormService);
  });

  describe('Service methods', () => {
    describe('createProductItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeClassId: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });

      it('passing IProductItem should create a new form with FormGroup', () => {
        const formGroup = service.createProductItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeClassId: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });
    });

    describe('getProductItem', () => {
      it('should return NewProductItem for default ProductItem initial value', () => {
        const formGroup = service.createProductItemFormGroup(sampleWithNewData);

        const productItem = service.getProductItem(formGroup) as any;

        expect(productItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductItem for empty ProductItem initial value', () => {
        const formGroup = service.createProductItemFormGroup();

        const productItem = service.getProductItem(formGroup) as any;

        expect(productItem).toMatchObject({});
      });

      it('should return IProductItem', () => {
        const formGroup = service.createProductItemFormGroup(sampleWithRequiredData);

        const productItem = service.getProductItem(formGroup) as any;

        expect(productItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductItem should not enable id FormControl', () => {
        const formGroup = service.createProductItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductItem should disable id FormControl', () => {
        const formGroup = service.createProductItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
