import { TestBed } from '@angular/core/testing';

import { RuntimeProcessDefinitionService } from './runtime-process-definition.service';

describe('RuntimeProcessDefinitionService', () => {
  let service: RuntimeProcessDefinitionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RuntimeProcessDefinitionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
