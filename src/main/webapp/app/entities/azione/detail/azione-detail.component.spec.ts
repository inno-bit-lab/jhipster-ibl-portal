import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AzioneDetailComponent } from './azione-detail.component';

describe('Azione Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AzioneDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AzioneDetailComponent,
              resolve: { azione: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AzioneDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load azione on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AzioneDetailComponent);

      // THEN
      expect(instance.azione).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
