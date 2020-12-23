import { createSelector } from "@ngrx/store";
import { getWcmAppState, WcmAppState } from "../reducers/wcm-authoring.reducer";
import { QueryState, QueryAdapter } from "../reducers/query.reducer";
export const {
  selectIds: _selectQueryIds,
  selectEntities: _selectQueryEntitie,
  selectAll: _selectAllQueries,
  selectTotal: _selectQueryTotal
} = QueryAdapter.getSelectors();

export const getQueryState = createSelector(
  getWcmAppState,
  (state: WcmAppState) => state.queryStatements
);

export const getQueryStatus = createSelector(
  getQueryState,
  (state: QueryState): string => state.status
);
export const getQueryLoading = createSelector(
  getQueryState,
  (state: QueryState): boolean => state.loading
);

export const getQueryTotal = createSelector(getQueryState, _selectQueryTotal);

export const getQueryEntities = createSelector(
  getQueryState,
  _selectQueryEntitie
);
export const getQueryByLibraryAndName = createSelector(
  getQueryEntities,
  (entities, props) => entities[`${props.library}_${props.name}`]
);
