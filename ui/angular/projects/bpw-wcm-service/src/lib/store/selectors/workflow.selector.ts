import { createSelector } from "@ngrx/store";
import { getWcmAppState, WcmAppState } from "../reducers/wcm-authoring.reducer";
import { WorkflowState, WorkflowAdapter } from "../reducers/workflow.reducer";
export const {
  selectIds: _selectWorkflowIds,
  selectEntities: _selectWorkflowEntitie,
  selectAll: _selectAllWorkflows,
  selectTotal: _selectWorkflowTotal
} = WorkflowAdapter.getSelectors();

export const getWorkflowState = createSelector(
  getWcmAppState,
  (state: WcmAppState) => state.workflows
);

export const getWorkflowStatus = createSelector(
  getWorkflowState,
  (state: WorkflowState): string => state.status
);
export const getWorkflowLoading = createSelector(
  getWorkflowState,
  (state: WorkflowState): boolean => state.loading
);

export const getWorkflowTotal = createSelector(
  getWorkflowState,
  _selectWorkflowTotal
);

export const getWorkflows = createSelector(
  getWorkflowState,
  _selectAllWorkflows
);

export const getWorkflowRuleEntities = createSelector(
  getWorkflowState,
  _selectWorkflowEntitie
);

export const getWorkflowByLibraryAndName = createSelector(
  getWorkflowRuleEntities,
  (entities, props) => entities[`${props.library}_${props.name}`]
);
