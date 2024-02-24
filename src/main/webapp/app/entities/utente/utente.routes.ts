import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UtenteComponent } from './list/utente.component';
import { UtenteDetailComponent } from './detail/utente-detail.component';
import { UtenteUpdateComponent } from './update/utente-update.component';
import UtenteResolve from './route/utente-routing-resolve.service';

const utenteRoute: Routes = [
  {
    path: '',
    component: UtenteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UtenteDetailComponent,
    resolve: {
      utente: UtenteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UtenteUpdateComponent,
    resolve: {
      utente: UtenteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UtenteUpdateComponent,
    resolve: {
      utente: UtenteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default utenteRoute;
