import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ruolo.test-samples';

import { RuoloFormService } from './ruolo-form.service';

describe('Ruolo Form Service', () => {
  let service: RuoloFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RuoloFormService);
  });

  describe('Service methods', () => {
    describe('createRuoloFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRuoloFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            created: expect.any(Object),
            modified: expect.any(Object),
            nomeAzione: expect.any(Object),
            azioni: expect.any(Object),
          }),
        );
      });

      it('passing IRuolo should create a new form with FormGroup', () => {
        const formGroup = service.createRuoloFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            created: expect.any(Object),
            modified: expect.any(Object),
            nomeAzione: expect.any(Object),
            azioni: expect.any(Object),
          }),
        );
      });
    });

    describe('getRuolo', () => {
      it('should return NewRuolo for default Ruolo initial value', () => {
        const formGroup = service.createRuoloFormGroup(sampleWithNewData);

        const ruolo = service.getRuolo(formGroup) as any;

        expect(ruolo).toMatchObject(sampleWithNewData);
      });

      it('should return NewRuolo for empty Ruolo initial value', () => {
        const formGroup = service.createRuoloFormGroup();

        const ruolo = service.getRuolo(formGroup) as any;

        expect(ruolo).toMatchObject({});
      });

      it('should return IRuolo', () => {
        const formGroup = service.createRuoloFormGroup(sampleWithRequiredData);

        const ruolo = service.getRuolo(formGroup) as any;

        expect(ruolo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRuolo should not enable id FormControl', () => {
        const formGroup = service.createRuoloFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRuolo should disable id FormControl', () => {
        const formGroup = service.createRuoloFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
