import { TestBed } from '@angular/core/testing';

import { DecisionDefinitionsService } from './decision-definitions.service';

describe('DecisionDefinitionsService', () => {
  let service: DecisionDefinitionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DecisionDefinitionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
