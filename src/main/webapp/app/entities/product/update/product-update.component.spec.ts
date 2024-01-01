import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IFileDocument } from 'app/entities/file-document/file-document.model';
import { FileDocumentService } from 'app/entities/file-document/service/file-document.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';
import { ProductFormService } from './product-form.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Product Management Update Component', () => {
  let comp: ProductUpdateComponent;
  let fixture: ComponentFixture<ProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productFormService: ProductFormService;
  let productService: ProductService;
  let fileDocumentService: FileDocumentService;
  let categoryService: CategoryService;
  let partyService: PartyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ProductUpdateComponent],
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
      .overrideTemplate(ProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFormService = TestBed.inject(ProductFormService);
    productService = TestBed.inject(ProductService);
    fileDocumentService = TestBed.inject(FileDocumentService);
    categoryService = TestBed.inject(CategoryService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const parent: IProduct = { id: 12172 };
      product.parent = parent;

      const productCollection: IProduct[] = [{ id: 18846 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [parent];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FileDocument query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const documents: IFileDocument[] = [{ id: 14146 }];
      product.documents = documents;

      const fileDocumentCollection: IFileDocument[] = [{ id: 7063 }];
      jest.spyOn(fileDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: fileDocumentCollection })));
      const additionalFileDocuments = [...documents];
      const expectedCollection: IFileDocument[] = [...additionalFileDocuments, ...fileDocumentCollection];
      jest.spyOn(fileDocumentService, 'addFileDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(fileDocumentService.query).toHaveBeenCalled();
      expect(fileDocumentService.addFileDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        fileDocumentCollection,
        ...additionalFileDocuments.map(expect.objectContaining),
      );
      expect(comp.fileDocumentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Category query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const category: ICategory = { id: 11459 };
      product.category = category;

      const categoryCollection: ICategory[] = [{ id: 12074 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        categoryCollection,
        ...additionalCategories.map(expect.objectContaining),
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Party query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const party: IParty = { id: 30559 };
      product.party = party;

      const partyCollection: IParty[] = [{ id: 24134 }];
      jest.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      jest.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(expect.objectContaining),
      );
      expect(comp.partiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const product: IProduct = { id: 456 };
      const parent: IProduct = { id: 27599 };
      product.parent = parent;
      const documents: IFileDocument = { id: 26162 };
      product.documents = [documents];
      const category: ICategory = { id: 6111 };
      product.category = category;
      const party: IParty = { id: 10540 };
      product.party = party;

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(parent);
      expect(comp.fileDocumentsSharedCollection).toContain(documents);
      expect(comp.categoriesSharedCollection).toContain(category);
      expect(comp.partiesSharedCollection).toContain(party);
      expect(comp.product).toEqual(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue(product);
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productService.update).toHaveBeenCalledWith(expect.objectContaining(product));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue({ id: null });
      jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(productService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productService.update).toHaveBeenCalled();
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

    describe('compareFileDocument', () => {
      it('Should forward to fileDocumentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(fileDocumentService, 'compareFileDocument');
        comp.compareFileDocument(entity, entity2);
        expect(fileDocumentService.compareFileDocument).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCategory', () => {
      it('Should forward to categoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoryService, 'compareCategory');
        comp.compareCategory(entity, entity2);
        expect(categoryService.compareCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareParty', () => {
      it('Should forward to partyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
