import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RuoloDetailComponent } from './ruolo-detail.component';

describe('Ruolo Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RuoloDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RuoloDetailComponent,
              resolve: { ruolo: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RuoloDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ruolo on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RuoloDetailComponent);

      // THEN
      expect(instance.ruolo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
