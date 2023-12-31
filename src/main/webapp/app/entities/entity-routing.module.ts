import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'class-type',
        data: { pageTitle: 'ecommercePortalApp.classType.home.title' },
        loadChildren: () => import('./class-type/class-type.routes'),
      },
      {
        path: 'classification',
        data: { pageTitle: 'ecommercePortalApp.classification.home.title' },
        loadChildren: () => import('./classification/classification.routes'),
      },
      {
        path: 'party',
        data: { pageTitle: 'ecommercePortalApp.party.home.title' },
        loadChildren: () => import('./party/party.routes'),
      },
      {
        path: 'campaign',
        data: { pageTitle: 'ecommercePortalApp.campaign.home.title' },
        loadChildren: () => import('./campaign/campaign.routes'),
      },
      {
        path: 'user-comment',
        data: { pageTitle: 'ecommercePortalApp.userComment.home.title' },
        loadChildren: () => import('./user-comment/user-comment.routes'),
      },
      {
        path: 'geo-division',
        data: { pageTitle: 'ecommercePortalApp.geoDivision.home.title' },
        loadChildren: () => import('./geo-division/geo-division.routes'),
      },
      {
        path: 'location',
        data: { pageTitle: 'ecommercePortalApp.location.home.title' },
        loadChildren: () => import('./location/location.routes'),
      },
      {
        path: 'contact',
        data: { pageTitle: 'ecommercePortalApp.contact.home.title' },
        loadChildren: () => import('./contact/contact.routes'),
      },
      {
        path: 'category',
        data: { pageTitle: 'ecommercePortalApp.category.home.title' },
        loadChildren: () => import('./category/category.routes'),
      },
      {
        path: 'product',
        data: { pageTitle: 'ecommercePortalApp.product.home.title' },
        loadChildren: () => import('./product/product.routes'),
      },
      {
        path: 'consume-material',
        data: { pageTitle: 'ecommercePortalApp.consumeMaterial.home.title' },
        loadChildren: () => import('./consume-material/consume-material.routes'),
      },
      {
        path: 'product-item',
        data: { pageTitle: 'ecommercePortalApp.productItem.home.title' },
        loadChildren: () => import('./product-item/product-item.routes'),
      },
      {
        path: 'agreement',
        data: { pageTitle: 'ecommercePortalApp.agreement.home.title' },
        loadChildren: () => import('./agreement/agreement.routes'),
      },
      {
        path: 'factor',
        data: { pageTitle: 'ecommercePortalApp.factor.home.title' },
        loadChildren: () => import('./factor/factor.routes'),
      },
      {
        path: 'factor-item',
        data: { pageTitle: 'ecommercePortalApp.factorItem.home.title' },
        loadChildren: () => import('./factor-item/factor-item.routes'),
      },
      {
        path: 'criticism',
        data: { pageTitle: 'ecommercePortalApp.criticism.home.title' },
        loadChildren: () => import('./criticism/criticism.routes'),
      },
      {
        path: 'file-document',
        data: { pageTitle: 'ecommercePortalApp.fileDocument.home.title' },
        loadChildren: () => import('./file-document/file-document.routes'),
      },
      {
        path: 'my-authority',
        data: { pageTitle: 'ecommercePortalApp.myAuthority.home.title' },
        loadChildren: () => import('./my-authority/my-authority.routes'),
      },
      {
        path: 'resource',
        data: { pageTitle: 'ecommercePortalApp.resource.home.title' },
        loadChildren: () => import('./resource/resource.routes'),
      },
      {
        path: 'resource-authority',
        data: { pageTitle: 'ecommercePortalApp.resourceAuthority.home.title' },
        loadChildren: () => import('./resource-authority/resource-authority.routes'),
      },
      {
        path: 'config',
        data: { pageTitle: 'ecommercePortalApp.config.home.title' },
        loadChildren: () => import('./config/config.routes'),
      },
      {
        path: 'user-favorite',
        data: { pageTitle: 'ecommercePortalApp.userFavorite.home.title' },
        loadChildren: () => import('./user-favorite/user-favorite.routes'),
      },
      {
        path: 'wallet',
        data: { pageTitle: 'ecommercePortalApp.wallet.home.title' },
        loadChildren: () => import('./wallet/wallet.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
