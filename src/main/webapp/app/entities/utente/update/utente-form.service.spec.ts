import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../utente.test-samples';

import { UtenteFormService } from './utente-form.service';

describe('Utente Form Service', () => {
  let service: UtenteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UtenteFormService);
  });

  describe('Service methods', () => {
    describe('createUtenteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUtenteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            created: expect.any(Object),
            modified: expect.any(Object),
            username: expect.any(Object),
            password: expect.any(Object),
            mail: expect.any(Object),
            mobile: expect.any(Object),
            facebook: expect.any(Object),
            google: expect.any(Object),
            instangram: expect.any(Object),
            provider: expect.any(Object),
            attivo: expect.any(Object),
            motivoBolocco: expect.any(Object),
            dataBolocco: expect.any(Object),
            registrationDate: expect.any(Object),
            lastAccess: expect.any(Object),
            ruolo: expect.any(Object),
          }),
        );
      });

      it('passing IUtente should create a new form with FormGroup', () => {
        const formGroup = service.createUtenteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            created: expect.any(Object),
            modified: expect.any(Object),
            username: expect.any(Object),
            password: expect.any(Object),
            mail: expect.any(Object),
            mobile: expect.any(Object),
            facebook: expect.any(Object),
            google: expect.any(Object),
            instangram: expect.any(Object),
            provider: expect.any(Object),
            attivo: expect.any(Object),
            motivoBolocco: expect.any(Object),
            dataBolocco: expect.any(Object),
            registrationDate: expect.any(Object),
            lastAccess: expect.any(Object),
            ruolo: expect.any(Object),
          }),
        );
      });
    });

    describe('getUtente', () => {
      it('should return NewUtente for default Utente initial value', () => {
        const formGroup = service.createUtenteFormGroup(sampleWithNewData);

        const utente = service.getUtente(formGroup) as any;

        expect(utente).toMatchObject(sampleWithNewData);
      });

      it('should return NewUtente for empty Utente initial value', () => {
        const formGroup = service.createUtenteFormGroup();

        const utente = service.getUtente(formGroup) as any;

        expect(utente).toMatchObject({});
      });

      it('should return IUtente', () => {
        const formGroup = service.createUtenteFormGroup(sampleWithRequiredData);

        const utente = service.getUtente(formGroup) as any;

        expect(utente).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUtente should not enable id FormControl', () => {
        const formGroup = service.createUtenteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUtente should disable id FormControl', () => {
        const formGroup = service.createUtenteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
