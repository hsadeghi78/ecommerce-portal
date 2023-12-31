import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProductItemComponent } from './list/product-item.component';
import { ProductItemDetailComponent } from './detail/product-item-detail.component';
import { ProductItemUpdateComponent } from './update/product-item-update.component';
import ProductItemResolve from './route/product-item-routing-resolve.service';

const productItemRoute: Routes = [
  {
    path: '',
    component: ProductItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductItemDetailComponent,
    resolve: {
      productItem: ProductItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductItemUpdateComponent,
    resolve: {
      productItem: ProductItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductItemUpdateComponent,
    resolve: {
      productItem: ProductItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productItemRoute;
