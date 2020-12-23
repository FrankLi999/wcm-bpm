import { TestBed } from '@angular/core/testing';

import { CaseDefinitionsService } from './case-definitions.service';

describe('CaseDefinitionsService', () => {
  let service: CaseDefinitionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CaseDefinitionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
