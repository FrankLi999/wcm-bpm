import { Action } from "@ngrx/store";

export enum CategoryActionTypes {
  CATEGORY_ACTION_SUCCESSFUL = "[Category] Action Successful",
  CATEGORY_ACTION_FAILED = "[Category] Action Failed",
  CATEGORY_CLEAR_ERROR = "[Category] Clear Error"
}

export class CategoryActionSuccessful implements Action {
  readonly type = CategoryActionTypes.CATEGORY_ACTION_SUCCESSFUL;
  constructor() {}
}

export class CategoryActionFailed implements Action {
  readonly type = CategoryActionTypes.CATEGORY_ACTION_FAILED;
  constructor(public payload: string) {}
}

export class CategoryClearError implements Action {
  readonly type = CategoryActionTypes.CATEGORY_CLEAR_ERROR;
  constructor() {}
}

export type CategorywActions =
  | CategoryActionSuccessful
  | CategoryActionFailed
  | CategoryClearError;
