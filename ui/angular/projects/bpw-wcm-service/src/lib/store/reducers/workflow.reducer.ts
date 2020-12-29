import { EntityState, EntityAdapter, createEntityAdapter } from "@ngrx/entity";
import {
  WorkflowActionTypes,
  WorkflowActions
} from "../actions/workflow.actions";
import { BpmnWorkflow } from "../../model/BpmnWorkflow";
import {
  WCM_ACTION_SUCCESSFUL,
  WCM_LOAD_SUCCESSFUL
} from "../../model/wcm-response";

export interface WorkflowState extends EntityState<BpmnWorkflow> {
  status?: any;
  loading: boolean;
  total: number;
}
export const workflowId = (item: BpmnWorkflow) =>
  `${item.library}_${item.name}`;

export const WorkflowAdapter: EntityAdapter<BpmnWorkflow> = createEntityAdapter<
  BpmnWorkflow
>({
  selectId: item => `${item.library}_${item.name}`
});

export const WorkflowInitialState: WorkflowState = WorkflowAdapter.getInitialState(
  {
    status: null,
    loading: true,
    total: 0
  }
);

export function WorkflowReducer(
  state = WorkflowInitialState,
  action: WorkflowActions
): WorkflowState {
  switch (action.type) {
    case WorkflowActionTypes.LOAD_WORKFLOW:
    case WorkflowActionTypes.CREATE_WORKFLOW:
    case WorkflowActionTypes.UPDATE_WORKFLOW:
    case WorkflowActionTypes.DELETE_WORKFLOW: {
      return {
        ...state,
        status: null
      };
    }
    case WorkflowActionTypes.LOAD_WORKFLOW_SUCCESSFUL: {
      return WorkflowAdapter.addAll(action.payload, {
        ...state,
        status: WCM_LOAD_SUCCESSFUL,
        loading: false
      });
    }
    case WorkflowActionTypes.CREATE_WORKFLOW_SUCCESSFUL: {
      return WorkflowAdapter.addOne(action.payload, {
        ...state,
        status: WCM_ACTION_SUCCESSFUL,
        loading: false
      });
    }
    case WorkflowActionTypes.UPDATE_WORKFLOW_SUCCESSFUL: {
      return WorkflowAdapter.upsertOne(action.payload, {
        ...state,
        status: WCM_ACTION_SUCCESSFUL,
        loading: false
      });
    }
    case WorkflowActionTypes.DELETE_WORKFLOW_SUCCESSFUL: {
      return WorkflowAdapter.removeOne(
        `${action.payload.library}_${action.payload.name}`,
        {
          ...state,
          status: WCM_ACTION_SUCCESSFUL,
          loading: false
        }
      );
    }
    case WorkflowActionTypes.LOAD_WORKFLOW_FAILED: {
      return WorkflowAdapter.removeAll({
        ...state,
        status: action.payload,
        loading: false
      });
    }
    case WorkflowActionTypes.WORKFLOW_ACTION_FAILED: {
      return {
        ...state,
        status: action.payload,
        loading: false
      };
    }
    case WorkflowActionTypes.WORKFLOW_CLEAR_ERROR:
      return {
        ...state,
        status: null
      };
    default:
      return state;
  }
}
