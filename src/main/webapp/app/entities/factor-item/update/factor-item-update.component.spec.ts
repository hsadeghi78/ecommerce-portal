import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IFactor } from 'app/entities/factor/factor.model';
import { FactorService } from 'app/entities/factor/service/factor.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IFactorItem } from '../factor-item.model';
import { FactorItemService } from '../service/factor-item.service';
import { FactorItemFormService } from './factor-item-form.service';

import { FactorItemUpdateComponent } from './factor-item-update.component';

describe('FactorItem Management Update Component', () => {
  let comp: FactorItemUpdateComponent;
  let fixture: ComponentFixture<FactorItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let factorItemFormService: FactorItemFormService;
  let factorItemService: FactorItemService;
  let factorService: FactorService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FactorItemUpdateComponent],
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
      .overrideTemplate(FactorItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FactorItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    factorItemFormService = TestBed.inject(FactorItemFormService);
    factorItemService = TestBed.inject(FactorItemService);
    factorService = TestBed.inject(FactorService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Factor query and add missing value', () => {
      const factorItem: IFactorItem = { id: 456 };
      const factor: IFactor = { id: 19912 };
      factorItem.factor = factor;

      const factorCollection: IFactor[] = [{ id: 8332 }];
      jest.spyOn(factorService, 'query').mockReturnValue(of(new HttpResponse({ body: factorCollection })));
      const additionalFactors = [factor];
      const expectedCollection: IFactor[] = [...additionalFactors, ...factorCollection];
      jest.spyOn(factorService, 'addFactorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factorItem });
      comp.ngOnInit();

      expect(factorService.query).toHaveBeenCalled();
      expect(factorService.addFactorToCollectionIfMissing).toHaveBeenCalledWith(
        factorCollection,
        ...additionalFactors.map(expect.objectContaining),
      );
      expect(comp.factorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const factorItem: IFactorItem = { id: 456 };
      const product: IProduct = { id: 24717 };
      factorItem.product = product;

      const productCollection: IProduct[] = [{ id: 2946 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factorItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const factorItem: IFactorItem = { id: 456 };
      const factor: IFactor = { id: 25099 };
      factorItem.factor = factor;
      const product: IProduct = { id: 31503 };
      factorItem.product = product;

      activatedRoute.data = of({ factorItem });
      comp.ngOnInit();

      expect(comp.factorsSharedCollection).toContain(factor);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.factorItem).toEqual(factorItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactorItem>>();
      const factorItem = { id: 123 };
      jest.spyOn(factorItemFormService, 'getFactorItem').mockReturnValue(factorItem);
      jest.spyOn(factorItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factorItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factorItem }));
      saveSubject.complete();

      // THEN
      expect(factorItemFormService.getFactorItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(factorItemService.update).toHaveBeenCalledWith(expect.objectContaining(factorItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactorItem>>();
      const factorItem = { id: 123 };
      jest.spyOn(factorItemFormService, 'getFactorItem').mockReturnValue({ id: null });
      jest.spyOn(factorItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factorItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factorItem }));
      saveSubject.complete();

      // THEN
      expect(factorItemFormService.getFactorItem).toHaveBeenCalled();
      expect(factorItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactorItem>>();
      const factorItem = { id: 123 };
      jest.spyOn(factorItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factorItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(factorItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFactor', () => {
      it('Should forward to factorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(factorService, 'compareFactor');
        comp.compareFactor(entity, entity2);
        expect(factorService.compareFactor).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
