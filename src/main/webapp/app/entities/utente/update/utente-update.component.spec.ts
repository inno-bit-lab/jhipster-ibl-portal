import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IRuolo } from 'app/entities/ruolo/ruolo.model';
import { RuoloService } from 'app/entities/ruolo/service/ruolo.service';
import { UtenteService } from '../service/utente.service';
import { IUtente } from '../utente.model';
import { UtenteFormService } from './utente-form.service';

import { UtenteUpdateComponent } from './utente-update.component';

describe('Utente Management Update Component', () => {
  let comp: UtenteUpdateComponent;
  let fixture: ComponentFixture<UtenteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let utenteFormService: UtenteFormService;
  let utenteService: UtenteService;
  let ruoloService: RuoloService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UtenteUpdateComponent],
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
      .overrideTemplate(UtenteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UtenteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    utenteFormService = TestBed.inject(UtenteFormService);
    utenteService = TestBed.inject(UtenteService);
    ruoloService = TestBed.inject(RuoloService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Ruolo query and add missing value', () => {
      const utente: IUtente = { id: 456 };
      const ruolo: IRuolo = { id: 5092 };
      utente.ruolo = ruolo;

      const ruoloCollection: IRuolo[] = [{ id: 19251 }];
      jest.spyOn(ruoloService, 'query').mockReturnValue(of(new HttpResponse({ body: ruoloCollection })));
      const additionalRuolos = [ruolo];
      const expectedCollection: IRuolo[] = [...additionalRuolos, ...ruoloCollection];
      jest.spyOn(ruoloService, 'addRuoloToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ utente });
      comp.ngOnInit();

      expect(ruoloService.query).toHaveBeenCalled();
      expect(ruoloService.addRuoloToCollectionIfMissing).toHaveBeenCalledWith(
        ruoloCollection,
        ...additionalRuolos.map(expect.objectContaining),
      );
      expect(comp.ruolosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const utente: IUtente = { id: 456 };
      const ruolo: IRuolo = { id: 12466 };
      utente.ruolo = ruolo;

      activatedRoute.data = of({ utente });
      comp.ngOnInit();

      expect(comp.ruolosSharedCollection).toContain(ruolo);
      expect(comp.utente).toEqual(utente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtente>>();
      const utente = { id: 123 };
      jest.spyOn(utenteFormService, 'getUtente').mockReturnValue(utente);
      jest.spyOn(utenteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utente }));
      saveSubject.complete();

      // THEN
      expect(utenteFormService.getUtente).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(utenteService.update).toHaveBeenCalledWith(expect.objectContaining(utente));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtente>>();
      const utente = { id: 123 };
      jest.spyOn(utenteFormService, 'getUtente').mockReturnValue({ id: null });
      jest.spyOn(utenteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utente: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utente }));
      saveSubject.complete();

      // THEN
      expect(utenteFormService.getUtente).toHaveBeenCalled();
      expect(utenteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtente>>();
      const utente = { id: 123 };
      jest.spyOn(utenteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(utenteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRuolo', () => {
      it('Should forward to ruoloService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ruoloService, 'compareRuolo');
        comp.compareRuolo(entity, entity2);
        expect(ruoloService.compareRuolo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
