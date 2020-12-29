import { Action } from "@ngrx/store";
import { Library } from "../../model/Library";
import { LibraryResponse } from "../../model/library-response";
import { LoadParameters } from "../../model/load-parameters";
import { WcmResponse } from "../../model/wcm-response";

export enum WcmLibraryActionTypes {
  CREATE_LIBRARY = "[Library] Create",
  UPDATE_LIBRARY = "[Library] Update",
  LOAD_LIBRARY = "[Library] Load",
  DELETE_LIBRARY = "[Library] Delete",
  LIBRARY_ACTION_SUCCESS = "[Lirary] Action Success",
  LIBRARY_ACTION_FAILED = "[Library] action Failed",
  LIBRARY_ACTION_CLEAR = "[Library] clear"
}

export class CreateLibrary implements Action {
  readonly type = WcmLibraryActionTypes.CREATE_LIBRARY;
  constructor(public payload: Library) {}
}
export class UpdateLibrary implements Action {
  readonly type = WcmLibraryActionTypes.UPDATE_LIBRARY;
  constructor(public payload: Library) {}
}

export class LoadLibrary implements Action {
  readonly type = WcmLibraryActionTypes.LOAD_LIBRARY;
  constructor(public payload: LoadParameters) {}
}

export class DeleteLibrary implements Action {
  readonly type = WcmLibraryActionTypes.DELETE_LIBRARY;
  constructor(public payload: Library) {}
}

export class LibraryActionSuccess implements Action {
  readonly type = WcmLibraryActionTypes.LIBRARY_ACTION_SUCCESS;
  constructor(public payload: LibraryResponse) {}
}

export class LibraryActionFailed implements Action {
  readonly type = WcmLibraryActionTypes.LIBRARY_ACTION_FAILED;
  constructor(public payload: WcmResponse) {}
}

export class LibraryActionClear implements Action {
  readonly type = WcmLibraryActionTypes.LIBRARY_ACTION_CLEAR;
  constructor() {}
}

export type WcmLibraryActions =
  | CreateLibrary
  | UpdateLibrary
  | LoadLibrary
  | DeleteLibrary
  | LibraryActionSuccess
  | LibraryActionFailed
  | LibraryActionClear;
