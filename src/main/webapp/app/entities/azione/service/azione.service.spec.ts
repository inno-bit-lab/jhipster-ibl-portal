import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAzione } from '../azione.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../azione.test-samples';

import { AzioneService, RestAzione } from './azione.service';

const requireRestSample: RestAzione = {
  ...sampleWithRequiredData,
  created: sampleWithRequiredData.created?.format(DATE_FORMAT),
  modified: sampleWithRequiredData.modified?.format(DATE_FORMAT),
};

describe('Azione Service', () => {
  let service: AzioneService;
  let httpMock: HttpTestingController;
  let expectedResult: IAzione | IAzione[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AzioneService);
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

    it('should create a Azione', () => {
      const azione = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(azione).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Azione', () => {
      const azione = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(azione).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Azione', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Azione', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Azione', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Azione', () => {
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

    describe('addAzioneToCollectionIfMissing', () => {
      it('should add a Azione to an empty array', () => {
        const azione: IAzione = sampleWithRequiredData;
        expectedResult = service.addAzioneToCollectionIfMissing([], azione);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(azione);
      });

      it('should not add a Azione to an array that contains it', () => {
        const azione: IAzione = sampleWithRequiredData;
        const azioneCollection: IAzione[] = [
          {
            ...azione,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAzioneToCollectionIfMissing(azioneCollection, azione);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Azione to an array that doesn't contain it", () => {
        const azione: IAzione = sampleWithRequiredData;
        const azioneCollection: IAzione[] = [sampleWithPartialData];
        expectedResult = service.addAzioneToCollectionIfMissing(azioneCollection, azione);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(azione);
      });

      it('should add only unique Azione to an array', () => {
        const azioneArray: IAzione[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const azioneCollection: IAzione[] = [sampleWithRequiredData];
        expectedResult = service.addAzioneToCollectionIfMissing(azioneCollection, ...azioneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const azione: IAzione = sampleWithRequiredData;
        const azione2: IAzione = sampleWithPartialData;
        expectedResult = service.addAzioneToCollectionIfMissing([], azione, azione2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(azione);
        expect(expectedResult).toContain(azione2);
      });

      it('should accept null and undefined values', () => {
        const azione: IAzione = sampleWithRequiredData;
        expectedResult = service.addAzioneToCollectionIfMissing([], null, azione, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(azione);
      });

      it('should return initial array if no Azione is added', () => {
        const azioneCollection: IAzione[] = [sampleWithRequiredData];
        expectedResult = service.addAzioneToCollectionIfMissing(azioneCollection, undefined, null);
        expect(expectedResult).toEqual(azioneCollection);
      });
    });

    describe('compareAzione', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAzione(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAzione(entity1, entity2);
        const compareResult2 = service.compareAzione(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAzione(entity1, entity2);
        const compareResult2 = service.compareAzione(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAzione(entity1, entity2);
        const compareResult2 = service.compareAzione(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
