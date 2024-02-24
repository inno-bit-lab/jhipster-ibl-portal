import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AzioneService } from '../service/azione.service';
import { IAzione } from '../azione.model';
import { AzioneFormService } from './azione-form.service';

import { AzioneUpdateComponent } from './azione-update.component';

describe('Azione Management Update Component', () => {
  let comp: AzioneUpdateComponent;
  let fixture: ComponentFixture<AzioneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let azioneFormService: AzioneFormService;
  let azioneService: AzioneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AzioneUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AzioneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AzioneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    azioneFormService = TestBed.inject(AzioneFormService);
    azioneService = TestBed.inject(AzioneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const azione: IAzione = { id: 456 };

      activatedRoute.data = of({ azione });
      comp.ngOnInit();

      expect(comp.azione).toEqual(azione);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAzione>>();
      const azione = { id: 123 };
      jest.spyOn(azioneFormService, 'getAzione').mockReturnValue(azione);
      jest.spyOn(azioneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ azione });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: azione }));
      saveSubject.complete();

      // THEN
      expect(azioneFormService.getAzione).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(azioneService.update).toHaveBeenCalledWith(expect.objectContaining(azione));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAzione>>();
      const azione = { id: 123 };
      jest.spyOn(azioneFormService, 'getAzione').mockReturnValue({ id: null });
      jest.spyOn(azioneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ azione: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: azione }));
      saveSubject.complete();

      // THEN
      expect(azioneFormService.getAzione).toHaveBeenCalled();
      expect(azioneService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAzione>>();
      const azione = { id: 123 };
      jest.spyOn(azioneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ azione });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(azioneService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
