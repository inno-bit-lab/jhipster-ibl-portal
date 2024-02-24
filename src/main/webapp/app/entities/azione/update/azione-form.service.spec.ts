import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../azione.test-samples';

import { AzioneFormService } from './azione-form.service';

describe('Azione Form Service', () => {
  let service: AzioneFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AzioneFormService);
  });

  describe('Service methods', () => {
    describe('createAzioneFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAzioneFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            created: expect.any(Object),
            modified: expect.any(Object),
            nomeAzione: expect.any(Object),
            descrizione: expect.any(Object),
          }),
        );
      });

      it('passing IAzione should create a new form with FormGroup', () => {
        const formGroup = service.createAzioneFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            created: expect.any(Object),
            modified: expect.any(Object),
            nomeAzione: expect.any(Object),
            descrizione: expect.any(Object),
          }),
        );
      });
    });

    describe('getAzione', () => {
      it('should return NewAzione for default Azione initial value', () => {
        const formGroup = service.createAzioneFormGroup(sampleWithNewData);

        const azione = service.getAzione(formGroup) as any;

        expect(azione).toMatchObject(sampleWithNewData);
      });

      it('should return NewAzione for empty Azione initial value', () => {
        const formGroup = service.createAzioneFormGroup();

        const azione = service.getAzione(formGroup) as any;

        expect(azione).toMatchObject({});
      });

      it('should return IAzione', () => {
        const formGroup = service.createAzioneFormGroup(sampleWithRequiredData);

        const azione = service.getAzione(formGroup) as any;

        expect(azione).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAzione should not enable id FormControl', () => {
        const formGroup = service.createAzioneFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAzione should disable id FormControl', () => {
        const formGroup = service.createAzioneFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
