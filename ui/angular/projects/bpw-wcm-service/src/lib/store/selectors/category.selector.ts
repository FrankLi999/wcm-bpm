import { createSelector } from "@ngrx/store";
import { getWcmAppState, WcmAppState } from "../reducers/wcm-authoring.reducer";
import { CategoryState } from "../reducers/category.reducer";

export const getCategoryState = createSelector(
  getWcmAppState,
  (state: WcmAppState) => state.categoryState
);

export const getCategoryStatus = createSelector(
  getCategoryState,
  (state: CategoryState) => {
    return {
      status: state.status,
      timestamp: state.timestamp
    };
  }
);
