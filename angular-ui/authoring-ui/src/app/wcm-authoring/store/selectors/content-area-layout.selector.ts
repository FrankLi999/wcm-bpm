import { createSelector } from '@ngrx/store';
import { getWcmAppState, ContentAreaLayoutState, WcmAppState } from '../reducers';
import { WcmSystem } from '../../model';
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