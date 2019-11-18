import { TestBed } from '@angular/core/testing';

import { WcmService } from './wcm.service';

describe('WcmService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WcmService = TestBed.get(WcmService);
    expect(service).toBeTruthy();
  });
});
