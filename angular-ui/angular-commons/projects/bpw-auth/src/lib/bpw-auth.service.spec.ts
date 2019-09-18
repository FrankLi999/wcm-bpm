import { TestBed } from '@angular/core/testing';

import { BpwAuthService } from './bpw-auth.service';

describe('BpwAuthService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BpwAuthService = TestBed.get(BpwAuthService);
    expect(service).toBeTruthy();
  });
});
