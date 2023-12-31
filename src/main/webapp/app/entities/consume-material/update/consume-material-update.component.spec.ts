import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ConsumeMaterialService } from '../service/consume-material.service';
import { IConsumeMaterial } from '../consume-material.model';
import { ConsumeMaterialFormService } from './consume-material-form.service';

import { ConsumeMaterialUpdateComponent } from './consume-material-update.component';

describe('ConsumeMaterial Management Update Component', () => {
  let comp: ConsumeMaterialUpdateComponent;
  let fixture: ComponentFixture<ConsumeMaterialUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let consumeMaterialFormService: ConsumeMaterialFormService;
  let consumeMaterialService: ConsumeMaterialService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ConsumeMaterialUpdateComponent],
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
      .overrideTemplate(ConsumeMaterialUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConsumeMaterialUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    consumeMaterialFormService = TestBed.inject(ConsumeMaterialFormService);
    consumeMaterialService = TestBed.inject(ConsumeMaterialService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const consumeMaterial: IConsumeMaterial = { id: 456 };
      const product: IProduct = { id: 32352 };
      consumeMaterial.product = product;

      const productCollection: IProduct[] = [{ id: 25530 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consumeMaterial });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const consumeMaterial: IConsumeMaterial = { id: 456 };
      const product: IProduct = { id: 5921 };
      consumeMaterial.product = product;

      activatedRoute.data = of({ consumeMaterial });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.consumeMaterial).toEqual(consumeMaterial);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsumeMaterial>>();
      const consumeMaterial = { id: 123 };
      jest.spyOn(consumeMaterialFormService, 'getConsumeMaterial').mockReturnValue(consumeMaterial);
      jest.spyOn(consumeMaterialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consumeMaterial });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consumeMaterial }));
      saveSubject.complete();

      // THEN
      expect(consumeMaterialFormService.getConsumeMaterial).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(consumeMaterialService.update).toHaveBeenCalledWith(expect.objectContaining(consumeMaterial));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsumeMaterial>>();
      const consumeMaterial = { id: 123 };
      jest.spyOn(consumeMaterialFormService, 'getConsumeMaterial').mockReturnValue({ id: null });
      jest.spyOn(consumeMaterialService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consumeMaterial: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consumeMaterial }));
      saveSubject.complete();

      // THEN
      expect(consumeMaterialFormService.getConsumeMaterial).toHaveBeenCalled();
      expect(consumeMaterialService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsumeMaterial>>();
      const consumeMaterial = { id: 123 };
      jest.spyOn(consumeMaterialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consumeMaterial });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(consumeMaterialService.update).toHaveBeenCalled();
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
