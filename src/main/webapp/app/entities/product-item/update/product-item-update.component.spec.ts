import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ProductItemService } from '../service/product-item.service';
import { IProductItem } from '../product-item.model';
import { ProductItemFormService } from './product-item-form.service';

import { ProductItemUpdateComponent } from './product-item-update.component';

describe('ProductItem Management Update Component', () => {
  let comp: ProductItemUpdateComponent;
  let fixture: ComponentFixture<ProductItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productItemFormService: ProductItemFormService;
  let productItemService: ProductItemService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ProductItemUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProductItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productItemFormService = TestBed.inject(ProductItemFormService);
    productItemService = TestBed.inject(ProductItemService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const productItem: IProductItem = { id: 456 };
      const product: IProduct = { id: 2994 };
      productItem.product = product;

      const productCollection: IProduct[] = [{ id: 28764 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productItem: IProductItem = { id: 456 };
      const product: IProduct = { id: 66 };
      productItem.product = product;

      activatedRoute.data = of({ productItem });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.productItem).toEqual(productItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductItem>>();
      const productItem = { id: 123 };
      jest.spyOn(productItemFormService, 'getProductItem').mockReturnValue(productItem);
      jest.spyOn(productItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productItem }));
      saveSubject.complete();

      // THEN
      expect(productItemFormService.getProductItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productItemService.update).toHaveBeenCalledWith(expect.objectContaining(productItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductItem>>();
      const productItem = { id: 123 };
      jest.spyOn(productItemFormService, 'getProductItem').mockReturnValue({ id: null });
      jest.spyOn(productItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productItem }));
      saveSubject.complete();

      // THEN
      expect(productItemFormService.getProductItem).toHaveBeenCalled();
      expect(productItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductItem>>();
      const productItem = { id: 123 };
      jest.spyOn(productItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
