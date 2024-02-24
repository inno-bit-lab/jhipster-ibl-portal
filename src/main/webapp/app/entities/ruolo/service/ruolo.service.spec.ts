import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRuolo } from '../ruolo.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ruolo.test-samples';

import { RuoloService, RestRuolo } from './ruolo.service';

const requireRestSample: RestRuolo = {
  ...sampleWithRequiredData,
  created: sampleWithRequiredData.created?.format(DATE_FORMAT),
  modified: sampleWithRequiredData.modified?.format(DATE_FORMAT),
};

describe('Ruolo Service', () => {
  let service: RuoloService;
  let httpMock: HttpTestingController;
  let expectedResult: IRuolo | IRuolo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RuoloService);
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

    it('should create a Ruolo', () => {
      const ruolo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ruolo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ruolo', () => {
      const ruolo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ruolo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ruolo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ruolo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ruolo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Ruolo', () => {
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

    describe('addRuoloToCollectionIfMissing', () => {
      it('should add a Ruolo to an empty array', () => {
        const ruolo: IRuolo = sampleWithRequiredData;
        expectedResult = service.addRuoloToCollectionIfMissing([], ruolo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ruolo);
      });

      it('should not add a Ruolo to an array that contains it', () => {
        const ruolo: IRuolo = sampleWithRequiredData;
        const ruoloCollection: IRuolo[] = [
          {
            ...ruolo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRuoloToCollectionIfMissing(ruoloCollection, ruolo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ruolo to an array that doesn't contain it", () => {
        const ruolo: IRuolo = sampleWithRequiredData;
        const ruoloCollection: IRuolo[] = [sampleWithPartialData];
        expectedResult = service.addRuoloToCollectionIfMissing(ruoloCollection, ruolo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ruolo);
      });

      it('should add only unique Ruolo to an array', () => {
        const ruoloArray: IRuolo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ruoloCollection: IRuolo[] = [sampleWithRequiredData];
        expectedResult = service.addRuoloToCollectionIfMissing(ruoloCollection, ...ruoloArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ruolo: IRuolo = sampleWithRequiredData;
        const ruolo2: IRuolo = sampleWithPartialData;
        expectedResult = service.addRuoloToCollectionIfMissing([], ruolo, ruolo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ruolo);
        expect(expectedResult).toContain(ruolo2);
      });

      it('should accept null and undefined values', () => {
        const ruolo: IRuolo = sampleWithRequiredData;
        expectedResult = service.addRuoloToCollectionIfMissing([], null, ruolo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ruolo);
      });

      it('should return initial array if no Ruolo is added', () => {
        const ruoloCollection: IRuolo[] = [sampleWithRequiredData];
        expectedResult = service.addRuoloToCollectionIfMissing(ruoloCollection, undefined, null);
        expect(expectedResult).toEqual(ruoloCollection);
      });
    });

    describe('compareRuolo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRuolo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRuolo(entity1, entity2);
        const compareResult2 = service.compareRuolo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRuolo(entity1, entity2);
        const compareResult2 = service.compareRuolo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRuolo(entity1, entity2);
        const compareResult2 = service.compareRuolo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
