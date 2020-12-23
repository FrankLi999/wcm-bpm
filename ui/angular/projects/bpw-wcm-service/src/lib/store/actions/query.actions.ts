import { Action } from "@ngrx/store";
import { QueryStatement } from "../../model/QueryStatement";
import { LoadParameters } from "../../model/load-parameters";

export enum QueryActionTypes {
  LOAD_QUERY = "[Query] LOAD",
  LOAD_QUERY_SUCCESSFUL = "[Query] LOAD Successful",
  LOAD_QUERY_FAILED = "[Query] LOAD FAILED",
  CREATE_QUERY = "[Query] Create",
  UPDATE_QUERY = "[Query] Update",
  CREATE_QUERY_SUCCESSFUL = "[Query] Create Successful",
  UPDATE_QUERY_SUCCESSFUL = "[Query] Update Successful",
  QUERY_ACTION_FAILED = "[Query] Action Failed",
  QUERY_CLEAR_ERROR = "[Query] Clear Error",
  DELETE_QUERY = "[Query] Remove",
  DELETE_QUERY_SUCCESSFUL = "[Query] Remove Successful",
}

export class LoadQuery implements Action {
  readonly type = QueryActionTypes.LOAD_QUERY;
  constructor(public payload: LoadParameters) {}
}

export class LoadQuerySuccessful implements Action {
  readonly type = QueryActionTypes.LOAD_QUERY_SUCCESSFUL;
  constructor(public payload: QueryStatement[]) {}
}

export class LoadQueryFailed implements Action {
  readonly type = QueryActionTypes.LOAD_QUERY_FAILED;
  constructor(public payload: string) {}
}

export class CreateQuery implements Action {
  readonly type = QueryActionTypes.CREATE_QUERY;
  constructor(public payload: QueryStatement) {}
}

export class UpdateQuery implements Action {
  readonly type = QueryActionTypes.UPDATE_QUERY;
  constructor(public payload: QueryStatement) {}
}

export class CreateQuerySuccessful implements Action {
  readonly type = QueryActionTypes.CREATE_QUERY_SUCCESSFUL;
  constructor(public payload: QueryStatement) {}
}

export class UpdateQuerySuccessful implements Action {
  readonly type = QueryActionTypes.UPDATE_QUERY_SUCCESSFUL;
  constructor(public payload: QueryStatement) {}
}

export class QueryActionFailed implements Action {
  readonly type = QueryActionTypes.QUERY_ACTION_FAILED;
  constructor(public payload: string) {}
}

export class DeleteQuery implements Action {
  readonly type = QueryActionTypes.DELETE_QUERY;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      wcmPath: string;
    }
  ) {}
}

export class DeleteQuerySuccessful implements Action {
  readonly type = QueryActionTypes.DELETE_QUERY_SUCCESSFUL;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      library: string;
      name: string;
    }
  ) {}
}

export class QueryClearError implements Action {
  readonly type = QueryActionTypes.QUERY_CLEAR_ERROR;
  constructor() {}
}

export type QueryActions =
  | LoadQuery
  | LoadQuerySuccessful
  | LoadQueryFailed
  | CreateQuery
  | UpdateQuery
  | DeleteQuery
  | CreateQuerySuccessful
  | UpdateQuerySuccessful
  | QueryActionFailed
  | DeleteQuerySuccessful
  | QueryClearError;
