import {
  ActionReducerMap,
  createFeatureSelector,
  createSelector,
} from "@ngrx/store";
import { WcmSystemReducer, WcmSystemState } from "./wcm-system.reducer";
import {
  WcmNavigationReducer,
  WcmNavigationState,
} from "./wcm-navigation.reducer";
import {
  ContentAreaLayoutState,
  ContentAreaLayoutReducer,
} from "./content-area-layout.reducer";
import { WcmLibraryState, WcmLibraryReducer } from "./wcm-library.reducer";

import { QueryState, QueryReducer } from "./query.reducer";
import { WorkflowState, WorkflowReducer } from "./workflow.reducer";
import { SiteConfigState, SiteConfigReducer } from "./site-config.reducer";
import {
  ValidationRuleState,
  ValidationRuleReducer,
} from "./validation-rule.reducer";

import { CategoryState, CategoryReducer } from "./category.reducer";

export const wcmAppFeatureKey = "wcmApp";

export interface WcmAppState {
  wcmSystem: WcmSystemState;
  contentAreaLayout: ContentAreaLayoutState;
  wcmLibraries: WcmLibraryState;
  wcmNavigation: WcmNavigationState;
  workflows: WorkflowState;
  validationRules: ValidationRuleState;
  queryStatements: QueryState;
  categoryState: CategoryState;
  siteConfigState: SiteConfigState;
}

export const reducers: ActionReducerMap<WcmAppState> = {
  wcmSystem: WcmSystemReducer,
  contentAreaLayout: ContentAreaLayoutReducer,
  wcmLibraries: WcmLibraryReducer,
  wcmNavigation: WcmNavigationReducer,
  workflows: WorkflowReducer,
  validationRules: ValidationRuleReducer,
  queryStatements: QueryReducer,
  categoryState: CategoryReducer,
  siteConfigState: SiteConfigReducer,
};

export const getWcmAppState = createFeatureSelector<WcmAppState>("wcmApp");

export const getAppState = createSelector(
  getWcmAppState,
  (state: WcmAppState) => state
);
