import { TestBed } from '@angular/core/testing';

import { JobDefinitionService } from './job-definition.service';

describe('JobDefinitionService', () => {
  let service: JobDefinitionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JobDefinitionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
