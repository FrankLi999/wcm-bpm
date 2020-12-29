import { Action } from "@ngrx/store";
import { BpmnWorkflow } from "../../model/BpmnWorkflow";
import { LoadParameters } from "../../model/load-parameters";

export enum WorkflowActionTypes {
  LOAD_WORKFLOW = "[Workflow] LOAD",
  LOAD_WORKFLOW_SUCCESSFUL = "[Workflow] LOAD Successful",
  LOAD_WORKFLOW_FAILED = "[Workflow] LOAD FAILED",
  CREATE_WORKFLOW = "[Workflow] Create",
  UPDATE_WORKFLOW = "[Workflow] Update",
  CREATE_WORKFLOW_SUCCESSFUL = "[Workflow] Create Successful",
  UPDATE_WORKFLOW_SUCCESSFUL = "[Workflow] Update Successful",
  WORKFLOW_ACTION_FAILED = "[Workflow] Action Failed",
  WORKFLOW_CLEAR_ERROR = "[Workflow] Clear Error",
  DELETE_WORKFLOW = "[Workflow] Remove",
  DELETE_WORKFLOW_SUCCESSFUL = "[Workflow] Remove Successful"
}

export class LoadWorkflow implements Action {
  readonly type = WorkflowActionTypes.LOAD_WORKFLOW;
  constructor(public payload: LoadParameters) {}
}

export class LoadWorkflowSuccessful implements Action {
  readonly type = WorkflowActionTypes.LOAD_WORKFLOW_SUCCESSFUL;
  constructor(public payload: BpmnWorkflow[]) {}
}

export class LoadWorkflowFailed implements Action {
  readonly type = WorkflowActionTypes.LOAD_WORKFLOW_FAILED;
  constructor(public payload: string) {}
}

export class CreateWorkflow implements Action {
  readonly type = WorkflowActionTypes.CREATE_WORKFLOW;
  constructor(public payload: BpmnWorkflow) {}
}

export class UpdateWorkflow implements Action {
  readonly type = WorkflowActionTypes.UPDATE_WORKFLOW;
  constructor(public payload: BpmnWorkflow) {}
}

export class CreateWorkflowSuccessful implements Action {
  readonly type = WorkflowActionTypes.CREATE_WORKFLOW_SUCCESSFUL;
  constructor(public payload: BpmnWorkflow) {}
}

export class UpdateWorkflowSuccessful implements Action {
  readonly type = WorkflowActionTypes.UPDATE_WORKFLOW_SUCCESSFUL;
  constructor(public payload: BpmnWorkflow) {}
}

export class WorkflowActionFailed implements Action {
  readonly type = WorkflowActionTypes.WORKFLOW_ACTION_FAILED;
  constructor(public payload: string) {}
}

export class DeleteWorkflow implements Action {
  readonly type = WorkflowActionTypes.DELETE_WORKFLOW;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      wcmPath: string;
    }
  ) {}
}

export class DeleteWorkflowSuccessful implements Action {
  readonly type = WorkflowActionTypes.DELETE_WORKFLOW_SUCCESSFUL;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      library: string;
      name: string;
    }
  ) {}
}

export class WorkflowClearError implements Action {
  readonly type = WorkflowActionTypes.WORKFLOW_CLEAR_ERROR;
  constructor() {}
}

export type WorkflowActions =
  | LoadWorkflow
  | LoadWorkflowSuccessful
  | LoadWorkflowFailed
  | CreateWorkflow
  | UpdateWorkflow
  | DeleteWorkflow
  | CreateWorkflowSuccessful
  | UpdateWorkflowSuccessful
  | WorkflowActionFailed
  | DeleteWorkflowSuccessful
  | WorkflowClearError;
