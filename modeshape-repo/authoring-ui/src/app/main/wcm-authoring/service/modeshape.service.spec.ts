import { TestBed } from '@angular/core/testing';

import { ModeshapeService } from './modeshape.service';

describe('ModeshapeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ModeshapeService = TestBed.get(ModeshapeService);
    expect(service).toBeTruthy();
  });
});
