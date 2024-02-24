import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAzione } from 'app/entities/azione/azione.model';
import { AzioneService } from 'app/entities/azione/service/azione.service';
import { RuoloService } from '../service/ruolo.service';
import { IRuolo } from '../ruolo.model';
import { RuoloFormService } from './ruolo-form.service';

import { RuoloUpdateComponent } from './ruolo-update.component';

describe('Ruolo Management Update Component', () => {
  let comp: RuoloUpdateComponent;
  let fixture: ComponentFixture<RuoloUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ruoloFormService: RuoloFormService;
  let ruoloService: RuoloService;
  let azioneService: AzioneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RuoloUpdateComponent],
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
      .overrideTemplate(RuoloUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RuoloUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ruoloFormService = TestBed.inject(RuoloFormService);
    ruoloService = TestBed.inject(RuoloService);
    azioneService = TestBed.inject(AzioneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Azione query and add missing value', () => {
      const ruolo: IRuolo = { id: 456 };
      const azioni: IAzione = { id: 16405 };
      ruolo.azioni = azioni;

      const azioneCollection: IAzione[] = [{ id: 3292 }];
      jest.spyOn(azioneService, 'query').mockReturnValue(of(new HttpResponse({ body: azioneCollection })));
      const additionalAziones = [azioni];
      const expectedCollection: IAzione[] = [...additionalAziones, ...azioneCollection];
      jest.spyOn(azioneService, 'addAzioneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ruolo });
      comp.ngOnInit();

      expect(azioneService.query).toHaveBeenCalled();
      expect(azioneService.addAzioneToCollectionIfMissing).toHaveBeenCalledWith(
        azioneCollection,
        ...additionalAziones.map(expect.objectContaining),
      );
      expect(comp.azionesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ruolo: IRuolo = { id: 456 };
      const azioni: IAzione = { id: 1374 };
      ruolo.azioni = azioni;

      activatedRoute.data = of({ ruolo });
      comp.ngOnInit();

      expect(comp.azionesSharedCollection).toContain(azioni);
      expect(comp.ruolo).toEqual(ruolo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRuolo>>();
      const ruolo = { id: 123 };
      jest.spyOn(ruoloFormService, 'getRuolo').mockReturnValue(ruolo);
      jest.spyOn(ruoloService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ruolo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ruolo }));
      saveSubject.complete();

      // THEN
      expect(ruoloFormService.getRuolo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ruoloService.update).toHaveBeenCalledWith(expect.objectContaining(ruolo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRuolo>>();
      const ruolo = { id: 123 };
      jest.spyOn(ruoloFormService, 'getRuolo').mockReturnValue({ id: null });
      jest.spyOn(ruoloService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ruolo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ruolo }));
      saveSubject.complete();

      // THEN
      expect(ruoloFormService.getRuolo).toHaveBeenCalled();
      expect(ruoloService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRuolo>>();
      const ruolo = { id: 123 };
      jest.spyOn(ruoloService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ruolo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ruoloService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAzione', () => {
      it('Should forward to azioneService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(azioneService, 'compareAzione');
        comp.compareAzione(entity, entity2);
        expect(azioneService.compareAzione).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
