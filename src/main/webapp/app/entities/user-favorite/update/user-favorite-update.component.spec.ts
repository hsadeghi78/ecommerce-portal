import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { UserFavoriteService } from '../service/user-favorite.service';
import { IUserFavorite } from '../user-favorite.model';
import { UserFavoriteFormService } from './user-favorite-form.service';

import { UserFavoriteUpdateComponent } from './user-favorite-update.component';

describe('UserFavorite Management Update Component', () => {
  let comp: UserFavoriteUpdateComponent;
  let fixture: ComponentFixture<UserFavoriteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userFavoriteFormService: UserFavoriteFormService;
  let userFavoriteService: UserFavoriteService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UserFavoriteUpdateComponent],
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
      .overrideTemplate(UserFavoriteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserFavoriteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userFavoriteFormService = TestBed.inject(UserFavoriteFormService);
    userFavoriteService = TestBed.inject(UserFavoriteService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const userFavorite: IUserFavorite = { id: 456 };
      const product: IProduct = { id: 19146 };
      userFavorite.product = product;

      const productCollection: IProduct[] = [{ id: 29916 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userFavorite });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userFavorite: IUserFavorite = { id: 456 };
      const product: IProduct = { id: 26311 };
      userFavorite.product = product;

      activatedRoute.data = of({ userFavorite });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.userFavorite).toEqual(userFavorite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserFavorite>>();
      const userFavorite = { id: 123 };
      jest.spyOn(userFavoriteFormService, 'getUserFavorite').mockReturnValue(userFavorite);
      jest.spyOn(userFavoriteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userFavorite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userFavorite }));
      saveSubject.complete();

      // THEN
      expect(userFavoriteFormService.getUserFavorite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userFavoriteService.update).toHaveBeenCalledWith(expect.objectContaining(userFavorite));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserFavorite>>();
      const userFavorite = { id: 123 };
      jest.spyOn(userFavoriteFormService, 'getUserFavorite').mockReturnValue({ id: null });
      jest.spyOn(userFavoriteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userFavorite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userFavorite }));
      saveSubject.complete();

      // THEN
      expect(userFavoriteFormService.getUserFavorite).toHaveBeenCalled();
      expect(userFavoriteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserFavorite>>();
      const userFavorite = { id: 123 };
      jest.spyOn(userFavoriteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userFavorite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userFavoriteService.update).toHaveBeenCalled();
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
