import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IFactor } from 'app/entities/factor/factor.model';
import { FactorService } from 'app/entities/factor/service/factor.service';
import { IUserComment } from '../user-comment.model';
import { UserCommentService } from '../service/user-comment.service';
import { UserCommentFormService } from './user-comment-form.service';

import { UserCommentUpdateComponent } from './user-comment-update.component';

describe('UserComment Management Update Component', () => {
  let comp: UserCommentUpdateComponent;
  let fixture: ComponentFixture<UserCommentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userCommentFormService: UserCommentFormService;
  let userCommentService: UserCommentService;
  let partyService: PartyService;
  let productService: ProductService;
  let factorService: FactorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UserCommentUpdateComponent],
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
      .overrideTemplate(UserCommentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserCommentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userCommentFormService = TestBed.inject(UserCommentFormService);
    userCommentService = TestBed.inject(UserCommentService);
    partyService = TestBed.inject(PartyService);
    productService = TestBed.inject(ProductService);
    factorService = TestBed.inject(FactorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserComment query and add missing value', () => {
      const userComment: IUserComment = { id: 456 };
      const parent: IUserComment = { id: 31768 };
      userComment.parent = parent;

      const userCommentCollection: IUserComment[] = [{ id: 8629 }];
      jest.spyOn(userCommentService, 'query').mockReturnValue(of(new HttpResponse({ body: userCommentCollection })));
      const additionalUserComments = [parent];
      const expectedCollection: IUserComment[] = [...additionalUserComments, ...userCommentCollection];
      jest.spyOn(userCommentService, 'addUserCommentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userComment });
      comp.ngOnInit();

      expect(userCommentService.query).toHaveBeenCalled();
      expect(userCommentService.addUserCommentToCollectionIfMissing).toHaveBeenCalledWith(
        userCommentCollection,
        ...additionalUserComments.map(expect.objectContaining),
      );
      expect(comp.userCommentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Party query and add missing value', () => {
      const userComment: IUserComment = { id: 456 };
      const party: IParty = { id: 5839 };
      userComment.party = party;

      const partyCollection: IParty[] = [{ id: 12528 }];
      jest.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      jest.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userComment });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(expect.objectContaining),
      );
      expect(comp.partiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const userComment: IUserComment = { id: 456 };
      const product: IProduct = { id: 8393 };
      userComment.product = product;

      const productCollection: IProduct[] = [{ id: 4166 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userComment });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Factor query and add missing value', () => {
      const userComment: IUserComment = { id: 456 };
      const factor: IFactor = { id: 4540 };
      userComment.factor = factor;

      const factorCollection: IFactor[] = [{ id: 32519 }];
      jest.spyOn(factorService, 'query').mockReturnValue(of(new HttpResponse({ body: factorCollection })));
      const additionalFactors = [factor];
      const expectedCollection: IFactor[] = [...additionalFactors, ...factorCollection];
      jest.spyOn(factorService, 'addFactorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userComment });
      comp.ngOnInit();

      expect(factorService.query).toHaveBeenCalled();
      expect(factorService.addFactorToCollectionIfMissing).toHaveBeenCalledWith(
        factorCollection,
        ...additionalFactors.map(expect.objectContaining),
      );
      expect(comp.factorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userComment: IUserComment = { id: 456 };
      const parent: IUserComment = { id: 21254 };
      userComment.parent = parent;
      const party: IParty = { id: 21472 };
      userComment.party = party;
      const product: IProduct = { id: 20541 };
      userComment.product = product;
      const factor: IFactor = { id: 6273 };
      userComment.factor = factor;

      activatedRoute.data = of({ userComment });
      comp.ngOnInit();

      expect(comp.userCommentsSharedCollection).toContain(parent);
      expect(comp.partiesSharedCollection).toContain(party);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.factorsSharedCollection).toContain(factor);
      expect(comp.userComment).toEqual(userComment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserComment>>();
      const userComment = { id: 123 };
      jest.spyOn(userCommentFormService, 'getUserComment').mockReturnValue(userComment);
      jest.spyOn(userCommentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userComment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userComment }));
      saveSubject.complete();

      // THEN
      expect(userCommentFormService.getUserComment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userCommentService.update).toHaveBeenCalledWith(expect.objectContaining(userComment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserComment>>();
      const userComment = { id: 123 };
      jest.spyOn(userCommentFormService, 'getUserComment').mockReturnValue({ id: null });
      jest.spyOn(userCommentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userComment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userComment }));
      saveSubject.complete();

      // THEN
      expect(userCommentFormService.getUserComment).toHaveBeenCalled();
      expect(userCommentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserComment>>();
      const userComment = { id: 123 };
      jest.spyOn(userCommentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userComment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userCommentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUserComment', () => {
      it('Should forward to userCommentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userCommentService, 'compareUserComment');
        comp.compareUserComment(entity, entity2);
        expect(userCommentService.compareUserComment).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFactor', () => {
      it('Should forward to factorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(factorService, 'compareFactor');
        comp.compareFactor(entity, entity2);
        expect(factorService.compareFactor).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
