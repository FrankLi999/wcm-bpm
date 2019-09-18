import { TestBed } from '@angular/core/testing';

import { BpwDynamicUiService } from './bpw-dynamic-ui.service';

describe('BpwDynamicUiService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BpwDynamicUiService = TestBed.get(BpwDynamicUiService);
    expect(service).toBeTruthy();
  });
});
