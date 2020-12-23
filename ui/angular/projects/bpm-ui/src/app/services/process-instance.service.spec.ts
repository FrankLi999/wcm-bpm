import { TestBed } from '@angular/core/testing';

import { ProcessInstanceService } from './process-instance.service';

describe('ProcessInstanceService', () => {
  let service: ProcessInstanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProcessInstanceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
