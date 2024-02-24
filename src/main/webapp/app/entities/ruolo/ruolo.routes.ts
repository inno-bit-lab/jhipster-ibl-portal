import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RuoloComponent } from './list/ruolo.component';
import { RuoloDetailComponent } from './detail/ruolo-detail.component';
import { RuoloUpdateComponent } from './update/ruolo-update.component';
import RuoloResolve from './route/ruolo-routing-resolve.service';

const ruoloRoute: Routes = [
  {
    path: '',
    component: RuoloComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RuoloDetailComponent,
    resolve: {
      ruolo: RuoloResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RuoloUpdateComponent,
    resolve: {
      ruolo: RuoloResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RuoloUpdateComponent,
    resolve: {
      ruolo: RuoloResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ruoloRoute;
