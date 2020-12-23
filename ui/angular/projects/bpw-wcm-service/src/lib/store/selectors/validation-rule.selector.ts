import { createSelector } from "@ngrx/store";
import { getWcmAppState, WcmAppState } from "../reducers/wcm-authoring.reducer";
import {
  ValidationRuleState,
  ValidationRuleAdapter
} from "../reducers/validation-rule.reducer";
export const {
  selectIds: _selectValidationRuleIds,
  selectEntities: _selectValidationRuleEntitie,
  selectAll: _selectAllValidationRules,
  selectTotal: _selectValidationRuleTotal
} = ValidationRuleAdapter.getSelectors();

export const getValidationRuleState = createSelector(
  getWcmAppState,
  (state: WcmAppState) => state.validationRules
);

export const getValidationRuleStatus = createSelector(
  getValidationRuleState,
  (state: ValidationRuleState): string => state.status
);

export const getValidationRuleLoading = createSelector(
  getValidationRuleState,
  (state: ValidationRuleState): boolean => state.loading
);

export const getValidationRuleTotal = createSelector(
  getValidationRuleState,
  _selectValidationRuleTotal
);

export const getValidationRules = createSelector(
  getValidationRuleState,
  _selectAllValidationRules
);

export const getValidationRuleEntities = createSelector(
  getValidationRuleState,
  _selectValidationRuleEntitie
);

export const getValidationRuleByLibraryAndName = createSelector(
  getValidationRuleEntities,
  (entities, props) => entities[`${props.library}_${props.name}`]
);
