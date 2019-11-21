import { ActionReducerMap, createFeatureSelector, createSelector } from '@ngrx/store';
import { WcmSystemReducer, WcmSystemState } from './wcm-system.reducer';
import { ContentAreaLayoutState, ContentAreaLayoutReducer } from './content-area-layout.reducer';

export const wcmAppFeatureKey = 'wcmApp';

export interface WcmAppState {
    wcmSystem: WcmSystemState;
    contentAreaLayout: ContentAreaLayoutState;
}

export const reducers: ActionReducerMap<WcmAppState> = {
    wcmSystem  : WcmSystemReducer,
    contentAreaLayout: ContentAreaLayoutReducer
};

export const getWcmAppState = createFeatureSelector<WcmAppState>(
    'wcmApp'
);

export const getAppState = createSelector(
    getWcmAppState,
    (state: WcmAppState) => state
);