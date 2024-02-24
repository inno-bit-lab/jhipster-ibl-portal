import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AzioneService } from '../service/azione.service';

import { AzioneComponent } from './azione.component';

describe('Azione Management Component', () => {
  let comp: AzioneComponent;
  let fixture: ComponentFixture<AzioneComponent>;
  let service: AzioneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'azione', component: AzioneComponent }]), HttpClientTestingModule, AzioneComponent],
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
      .overrideTemplate(AzioneComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AzioneComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AzioneService);

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
    expect(comp.aziones?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to azioneService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getAzioneIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAzioneIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
