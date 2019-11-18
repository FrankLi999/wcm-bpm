import { Action } from '@ngrx/store';
import { ContentAreaLayout } from '../../model/';
export enum ContentAreaLayoutActionTypes {
  CREATE_CONTENT_AREA_LAYOUT = '[ContentAreaLayout] Create',  
  CREATE_CONTENT_AREA_LAYOUT_SUCCESS = '[ContentAreaLayout] Create Success',  
  CREATE_CONTENT_AREA_LAYOUT_FAILED = '[ContentAreaLayout] Create Failed',  
  CONTENT_AREA_LAYOUT_CLEAR_ERROR = '[ContentAreaLayout] Clear Error',
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
  constructor(public payload: string) {
  }
}

export class RemoveContentAreaLayout implements Action {
  readonly type = ContentAreaLayoutActionTypes.REMOVE_CONTENT_AREA_LAYOUT;
  constructor(public payload: ContentAreaLayout) {
  }
}

export class ContentAreaLayoutClearError implements Action {
  readonly type = ContentAreaLayoutActionTypes.CONTENT_AREA_LAYOUT_CLEAR_ERROR;
  constructor() {
  }
}

export type ContentAreaLayoutActions = 
  CreateContentAreaLayout | 
  RemoveContentAreaLayout | 
  CreateContentAreaLayoutSuccess | 
  CreateContentAreaLayoutFailed |
  ContentAreaLayoutClearError;