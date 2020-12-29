import { Injectable } from "@angular/core";
import { Actions, Effect, ofType } from "@ngrx/effects";
import { of, Observable } from "rxjs";
import { switchMap, map, catchError, filter } from "rxjs/operators";
import { WorkflowService } from "../../service/workflow.service";
import { WcmUtils } from "../../utils/wcm-utils";
import { BpmnWorkflow } from "../../model/BpmnWorkflow";

import {
  WorkflowActionTypes,
  WorkflowActions,
  CreateWorkflow,
  UpdateWorkflow,
  LoadWorkflow,
  LoadWorkflowSuccessful,
  LoadWorkflowFailed,
  DeleteWorkflow,
  CreateWorkflowSuccessful,
  UpdateWorkflowSuccessful,
  WorkflowActionFailed,
  DeleteWorkflowSuccessful
} from "../actions/workflow.actions";

@Injectable()
export class WorkflowEffects {
  constructor(
    private actions$: Actions<WorkflowActions>,
    private workflowService: WorkflowService
  ) {}

  @Effect()
  loadWorkflow$: Observable<WorkflowActions> = this.actions$.pipe(
    ofType<LoadWorkflow>(WorkflowActionTypes.LOAD_WORKFLOW),
    switchMap(action => {
      return this.workflowService
        .loadBpmnWorkflows(
          action.payload.repository,
          action.payload.workspace,
          action.payload.filter,
          action.payload.sortDirection,
          action.payload.pageIndex,
          action.payload.pageSize
        )
        .pipe(
          filter(resp => resp != null),
          map((workflows: BpmnWorkflow[]) => {
            return new LoadWorkflowSuccessful(workflows);
          }),
          catchError(err => {
            return of(new LoadWorkflowFailed(err));
          })
        );
    })
  );
  @Effect()
  createWorkflow$: Observable<WorkflowActions> = this.actions$.pipe(
    ofType<CreateWorkflow>(WorkflowActionTypes.CREATE_WORKFLOW),
    switchMap(action => {
      return this.workflowService.createBpmnWorkflow(action.payload).pipe(
        filter(resp => resp !== undefined),
        map((response: any) => {
          return new CreateWorkflowSuccessful(action.payload);
        }),
        catchError(err => {
          return of(new WorkflowActionFailed(err));
        })
      );
    })
  );
  @Effect()
  updateWorkflow$: Observable<WorkflowActions> = this.actions$.pipe(
    ofType<UpdateWorkflow>(WorkflowActionTypes.UPDATE_WORKFLOW),
    switchMap(action => {
      return this.workflowService.saveBpmnWorkflow(action.payload).pipe(
        filter(resp => resp !== undefined),
        map((response: any) => {
          return new UpdateWorkflowSuccessful(action.payload);
        }),
        catchError(err => {
          return of(new WorkflowActionFailed(err));
        })
      );
    })
  );
  @Effect()
  deleteWorkflow$: Observable<WorkflowActions> = this.actions$.pipe(
    ofType<DeleteWorkflow>(WorkflowActionTypes.DELETE_WORKFLOW),
    switchMap(action => {
      return this.workflowService
        .purgeWorkflow(
          action.payload.repository,
          action.payload.workspace,
          action.payload.wcmPath
        )
        .pipe(
          filter(resp => resp !== undefined),
          map((response: any) => {
            return new DeleteWorkflowSuccessful({
              repository: action.payload.repository,
              workspace: action.payload.workspace,
              library: WcmUtils.library(action.payload.wcmPath),
              name: WcmUtils.itemName(action.payload.wcmPath)
            });
          }),
          catchError(err => {
            return of(new WorkflowActionFailed(err));
          })
        );
    })
  );
}
