import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AzioneComponent } from './list/azione.component';
import { AzioneDetailComponent } from './detail/azione-detail.component';
import { AzioneUpdateComponent } from './update/azione-update.component';
import AzioneResolve from './route/azione-routing-resolve.service';

const azioneRoute: Routes = [
  {
    path: '',
    component: AzioneComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AzioneDetailComponent,
    resolve: {
      azione: AzioneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AzioneUpdateComponent,
    resolve: {
      azione: AzioneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AzioneUpdateComponent,
    resolve: {
      azione: AzioneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default azioneRoute;
