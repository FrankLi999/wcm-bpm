import { TestBed } from '@angular/core/testing';
import { provideMockActions } from '@ngrx/effects/testing';
import { Observable } from 'rxjs';

import { WcmSystemEffects } from './wcm-system.effects';

describe('WcmAppEffects', () => {
  let actions$: Observable<any>;
  let effects: WcmSystemEffects;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        WcmSystemEffects,
        provideMockActions(() => actions$)
      ]
    });

    effects = TestBed.get<WcmSystemEffects>(WcmSystemEffects);
  });

  it('should be created', () => {
    expect(effects).toBeTruthy();
  });
});
