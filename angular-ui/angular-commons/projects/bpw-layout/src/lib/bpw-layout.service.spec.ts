import { TestBed } from '@angular/core/testing';

import { BpwLayoutService } from './bpw-layout.service';

describe('BpwLayoutService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BpwLayoutService = TestBed.get(BpwLayoutService);
    expect(service).toBeTruthy();
  });
});
