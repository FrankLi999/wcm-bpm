import { createSelector } from "@ngrx/store";
import { getWcmAppState, WcmAppState } from "../reducers/wcm-authoring.reducer";
import {
  WcmLibraryState,
  WcmLibraryAdapter
} from "../reducers/wcm-library.reducer";
export const {
  selectIds: _selectWcmLibraryIds,
  selectEntities: _selectWcmLibraryEntitie,
  selectAll: _selectAllLibraries,
  selectTotal: _selectLibraryTotal
} = WcmLibraryAdapter.getSelectors();

export const getWcmLibraryState = createSelector(
  getWcmAppState,
  (state: WcmAppState) => state.wcmLibraries
);

export const getWcmLibraryStatus = createSelector(
  getWcmLibraryState,
  (state: WcmLibraryState): string => state.status
);
export const getWcmLibraryLoading = createSelector(
  getWcmLibraryState,
  (state: WcmLibraryState): boolean => state.loading
);

export const getWcmLibraryTotal = createSelector(
  getWcmLibraryState,
  _selectLibraryTotal
);

export const getWcmLibraries = createSelector(
  getWcmLibraryState,
  _selectAllLibraries
);
