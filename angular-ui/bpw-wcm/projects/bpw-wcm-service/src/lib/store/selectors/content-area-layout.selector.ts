import { createSelector } from '@ngrx/store';
import { getWcmAppState, WcmAppState } from '../reducers/wcm-authoring.reducer';
import { ContentAreaLayoutState } from '../reducers/content-area-layout.reducer';
export const getContentAreaLayoutState = createSelector(
    getWcmAppState,
    (state: WcmAppState) => state.contentAreaLayout
);

export const getContentAreaLayout = createSelector(
    getContentAreaLayoutState,
    (state: ContentAreaLayoutState) => state.contentAreaLayout
);

export const getContentAreaLayoutError = createSelector(
    getContentAreaLayoutState,
    (state: ContentAreaLayoutState) => state.error
);