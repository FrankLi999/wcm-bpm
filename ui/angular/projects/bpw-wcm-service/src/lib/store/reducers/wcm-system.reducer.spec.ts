import { WcmSystemReducer, WcmSystemInitialState } from './wcm-system.reducer';

describe('WcmApp Reducer', () => {
  describe('an unknown action', () => {
    it('should return the previous state', () => {
      const action = {} as any;

      const result = WcmSystemReducer(WcmSystemInitialState, action);

      expect(result).toBe(WcmSystemInitialState);
    });
  });
});
