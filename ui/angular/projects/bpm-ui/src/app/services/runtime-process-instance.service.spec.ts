import { TestBed } from '@angular/core/testing';

import { RuntimeProcessInstanceService } from './runtime-process-instance.service';

describe('RuntimeProcessInstanceService', () => {
  let service: RuntimeProcessInstanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RuntimeProcessInstanceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
