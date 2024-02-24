import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IUtente } from '../utente.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../utente.test-samples';

import { UtenteService, RestUtente } from './utente.service';

const requireRestSample: RestUtente = {
  ...sampleWithRequiredData,
  created: sampleWithRequiredData.created?.format(DATE_FORMAT),
  modified: sampleWithRequiredData.modified?.format(DATE_FORMAT),
  dataBolocco: sampleWithRequiredData.dataBolocco?.format(DATE_FORMAT),
  registrationDate: sampleWithRequiredData.registrationDate?.format(DATE_FORMAT),
  lastAccess: sampleWithRequiredData.lastAccess?.format(DATE_FORMAT),
};

describe('Utente Service', () => {
  let service: UtenteService;
  let httpMock: HttpTestingController;
  let expectedResult: IUtente | IUtente[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UtenteService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Utente', () => {
      const utente = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(utente).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Utente', () => {
      const utente = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(utente).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Utente', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Utente', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Utente', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Utente', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addUtenteToCollectionIfMissing', () => {
      it('should add a Utente to an empty array', () => {
        const utente: IUtente = sampleWithRequiredData;
        expectedResult = service.addUtenteToCollectionIfMissing([], utente);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utente);
      });

      it('should not add a Utente to an array that contains it', () => {
        const utente: IUtente = sampleWithRequiredData;
        const utenteCollection: IUtente[] = [
          {
            ...utente,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUtenteToCollectionIfMissing(utenteCollection, utente);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Utente to an array that doesn't contain it", () => {
        const utente: IUtente = sampleWithRequiredData;
        const utenteCollection: IUtente[] = [sampleWithPartialData];
        expectedResult = service.addUtenteToCollectionIfMissing(utenteCollection, utente);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utente);
      });

      it('should add only unique Utente to an array', () => {
        const utenteArray: IUtente[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const utenteCollection: IUtente[] = [sampleWithRequiredData];
        expectedResult = service.addUtenteToCollectionIfMissing(utenteCollection, ...utenteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const utente: IUtente = sampleWithRequiredData;
        const utente2: IUtente = sampleWithPartialData;
        expectedResult = service.addUtenteToCollectionIfMissing([], utente, utente2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utente);
        expect(expectedResult).toContain(utente2);
      });

      it('should accept null and undefined values', () => {
        const utente: IUtente = sampleWithRequiredData;
        expectedResult = service.addUtenteToCollectionIfMissing([], null, utente, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utente);
      });

      it('should return initial array if no Utente is added', () => {
        const utenteCollection: IUtente[] = [sampleWithRequiredData];
        expectedResult = service.addUtenteToCollectionIfMissing(utenteCollection, undefined, null);
        expect(expectedResult).toEqual(utenteCollection);
      });
    });

    describe('compareUtente', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUtente(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUtente(entity1, entity2);
        const compareResult2 = service.compareUtente(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUtente(entity1, entity2);
        const compareResult2 = service.compareUtente(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUtente(entity1, entity2);
        const compareResult2 = service.compareUtente(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
