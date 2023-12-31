import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FileDocumentComponent } from './list/file-document.component';
import { FileDocumentDetailComponent } from './detail/file-document-detail.component';
import { FileDocumentUpdateComponent } from './update/file-document-update.component';
import FileDocumentResolve from './route/file-document-routing-resolve.service';

const fileDocumentRoute: Routes = [
  {
    path: '',
    component: FileDocumentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FileDocumentDetailComponent,
    resolve: {
      fileDocument: FileDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FileDocumentUpdateComponent,
    resolve: {
      fileDocument: FileDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FileDocumentUpdateComponent,
    resolve: {
      fileDocument: FileDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default fileDocumentRoute;
