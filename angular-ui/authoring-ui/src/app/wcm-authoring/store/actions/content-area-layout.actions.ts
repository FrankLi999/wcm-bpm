import { Action } from '@ngrx/store';
import { ContentAreaLayout } from '../../model/';
export enum ContentAreaLayoutActionTypes {
  CREATE_CONTENT_AREA_LAYOUT = '[ContentAreaLayout] Create',  
  CREATE_CONTENT_AREA_LAYOUT_SUCCESS = '[ContentAreaLayout] Create Success',  
  CREATE_CONTENT_AREA_LAYOUT_FAILED = '[ContentAreaLayout] Create Failed',  
  REMOVE_CONTENT_AREA_LAYOUT = '[ContentAreaLayout] Remove'
}

export class CreateContentAreaLayout implements Action {
  readonly type = ContentAreaLayoutActionTypes.CREATE_CONTENT_AREA_LAYOUT;
  constructor(public payload: ContentAreaLayout) {
  }
}

export class CreateContentAreaLayoutSuccess implements Action {
  readonly type = ContentAreaLayoutActionTypes.CREATE_CONTENT_AREA_LAYOUT_SUCCESS;
  constructor(public payload: ContentAreaLayout) {
  }
}

export class CreateContentAreaLayoutFailed implements Action {
  readonly type = ContentAreaLayoutActionTypes.CREATE_CONTENT_AREA_LAYOUT_FAILED;
  constructor(public payload: ContentAreaLayout) {
  }
}

export class RemoveContentAreaLayout implements Action {
  readonly type = ContentAreaLayoutActionTypes.REMOVE_CONTENT_AREA_LAYOUT;
  constructor(public payload: ContentAreaLayout) {
  }
}

export type ContentAreaLayoutActions = 
  CreateContentAreaLayout | 
  RemoveContentAreaLayout | 
  CreateContentAreaLayoutSuccess | 
  CreateContentAreaLayoutFailed;