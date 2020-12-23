import { TestBed } from '@angular/core/testing';

import { RuntimeIncidentService } from './runtime-incident.service';

describe('RuntimeIncidentService', () => {
  let service: RuntimeIncidentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RuntimeIncidentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
