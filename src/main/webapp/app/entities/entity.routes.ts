import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'azione',
    data: { pageTitle: 'iblPortalApp.azione.home.title' },
    loadChildren: () => import('./azione/azione.routes'),
  },
  {
    path: 'ruolo',
    data: { pageTitle: 'iblPortalApp.ruolo.home.title' },
    loadChildren: () => import('./ruolo/ruolo.routes'),
  },
  {
    path: 'utente',
    data: { pageTitle: 'iblPortalApp.utente.home.title' },
    loadChildren: () => import('./utente/utente.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
