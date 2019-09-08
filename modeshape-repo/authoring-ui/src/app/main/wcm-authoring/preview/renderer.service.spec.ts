import { TestBed } from '@angular/core/testing';

import { RendererService } from './renderer.service';

describe('RendererService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RendererService = TestBed.get(RendererService);
    expect(service).toBeTruthy();
  });
});
