import { TestBed } from '@angular/core/testing';

import { BpwComponentsService } from './bpw-components.service';

describe('BpwComponentsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BpwComponentsService = TestBed.get(BpwComponentsService);
    expect(service).toBeTruthy();
  });
});
