import { createSelector } from "@ngrx/store";
import { getWcmAppState, WcmAppState } from "../reducers/wcm-authoring.reducer";
import { WcmNavigationState } from "../reducers/wcm-navigation.reducer";
export const getWcmNavigationState = createSelector(
  getWcmAppState,
  (state: WcmAppState) => state.wcmNavigation
);

export const getCurrentWcmItem = createSelector(
  getWcmNavigationState,
  (state: WcmNavigationState) => state.currentNode
);

export const getCurrentNode = createSelector(
  getWcmNavigationState,
  (state: WcmNavigationState) => {
    return {
      currentNode: state.currentNode,
      children: state.children
    };
  }
);
