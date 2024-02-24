import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RuoloService } from '../service/ruolo.service';

import { RuoloComponent } from './ruolo.component';

describe('Ruolo Management Component', () => {
  let comp: RuoloComponent;
  let fixture: ComponentFixture<RuoloComponent>;
  let service: RuoloService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'ruolo', component: RuoloComponent }]), HttpClientTestingModule, RuoloComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(RuoloComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RuoloComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RuoloService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.ruolos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to ruoloService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getRuoloIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getRuoloIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
