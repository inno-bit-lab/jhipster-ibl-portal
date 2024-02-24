import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UtenteDetailComponent } from './utente-detail.component';

describe('Utente Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UtenteDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UtenteDetailComponent,
              resolve: { utente: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UtenteDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load utente on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UtenteDetailComponent);

      // THEN
      expect(instance.utente).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
