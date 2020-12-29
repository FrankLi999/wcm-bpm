import { TestBed } from '@angular/core/testing';

import { ProcessDefinitionsService } from './process-definitions.service';

describe('ProcessDefinitionsService', () => {
  let service: ProcessDefinitionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProcessDefinitionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
