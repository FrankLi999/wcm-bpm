import { TestBed } from '@angular/core/testing';

import { BpwFormService } from './bpw-form.service';

describe('BpwFormService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BpwFormService = TestBed.get(BpwFormService);
    expect(service).toBeTruthy();
  });
});
