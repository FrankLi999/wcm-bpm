import { Action } from "@ngrx/store";
import { CurrentSite } from "../../model/CurrentSite";
import { RenderTemplate } from "../../model/RenderTemplate";
import { AuthoringTemplate } from "../../model/AuthoringTemplate";
import { Form } from "../../model/Form";
import { SiteArea } from "../../model/SiteArea";
import { ContentAreaLayout } from "../../model/ContentAreaLayout";

export enum WcmSystemActionTypes {
  GET_WCMSYSTEM = "[WcmSystem] Get WcmSystem",
  GET_WCMSYSTEM_FOR_AUTHORING = "[WcmSystem] Get WcmSystem For Authoring",
  GET_WCMSYSTEM_SUCCESS = "[WcmSystem] Get WcmSystem Success",
  GET_WCMSYSTEM_FAILED = "[WcmSystem] Get WcmSystem Failed",
  UPDATE_SITE_AREA_LAYOUT = "[WcmSystem] Update Sitearea Layout",
  WCMSYSTEM_CLEAR_ERROR = "[WcmSystem] Clear Error",
  CREATE_RENDER_TEMPLATE = "[WcmSystem - RenderTemplate] Create",
  UPDATE_RENDER_TEMPLATE = "[WcmSystem - RenderTemplate] Update",
  DELETE_RENDER_TEMPLATE = "[WcmSystem - RenderTemplate] Delete",
  RENDER_TEMPLATE_ACTION_SUCCESSFUL = "[WcmSystem- RenderTemplate] Action Successful",
  RENDER_TEMPLATE_ACTION_FAILED = "[WcmSystem- RenderTemplate] Action Failed",
  DELETE_RENDER_TEMPLATE_SUCCESSFUL = "[WcmSystem- RenderTemplate] Delete Successful",
  RENDER_TEMPLATE_CLEAR_ERROR = "[WcmSystem- RenderTemplate] Clear Error",

  CREATE_AUTHORING_TEMPLATE = "[WcmSystem - AuthoringTemplate] Create",
  UPDATE_AUTHORING_TEMPLATE = "[WcmSystem - AuthoringTemplate] Update",
  DELETE_AUTHORING_TEMPLATE = "[WcmSystem - AuthoringTemplate] Delete",
  AUTHORING_TEMPLATE_FAILED = "[WcmSystem - AuthoringTemplate] Failed",
  AUTHORING_TEMPLATE_SUCCESSFUL = "[WcmSystem - AuthoringTemplate] Successful",
  DELETE_AUTHORING_TEMPLATE_SUCCESSFUL = "[WcmSystem - AuthoringTemplate] Delete Successful",
  AUTHORING_TEMPLATE_CLEAR_ERROR = "[WcmSystem - AuthoringTemplate] Clear Error",

  CREATE_FORM = "[WcmSystem - Form] Create",
  UPDATE_FORM = "[WcmSystem - Form] Update",
  DELETE_FORM = "[WcmSystem - Form] Delete",
  FORM_FAILED = "[WcmSystem - Form] Failed",
  FORM_SUCCESSFUL = "[WcmSystem - Form] Successful",
  DELETE_FORM_SUCCESSFUL = "[WcmSystem - Form] Delete Successful",
  FORM_CLEAR_ERROR = "[WcmSystem - Form] Clear Error",

  CREATE_CONTENT_AREA_LAYOUT = "[ContentAreaLayout] Create",
  CREATE_NEW_CONTENT_AREA_LAYOUT = "[ContentAreaLayout] New",
  EDIT_CONTENT_AREA_LAYOUT = "[ContentAreaLayout] Edit",
  UPDATE_CONTENT_AREA_LAYOUT = "[ContentAreaLayout] Update",
  CONTENT_AREA_LAYOUT_SUCCESSFUL = "[ContentAreaLayout] Action Successful",
  CONTENT_AREA_LAYOUT_FAILED = "[ContentAreaLayout] Action Failed",
  CONTENT_AREA_LAYOUT_CLEAR_ERROR = "[ContentAreaLayout] Clear Error",
  REMOVE_CONTENT_AREA_LAYOUT = "[ContentAreaLayout] Remove",
  REMOVE_CONTENT_AREA_LAYOUT_SUCCESSFUL = "[ContentAreaLayout] Remove Successful",

  CREATE_WORKFLOW = "[Workflow] Create",
  UPDATE_WORKFLOW = "[Workflow] Update",
  WORKFLOW_SUCCESSFUL = "[Workflow] Action Successful",
  WORKFLOW_FAILED = "[Workflow] Action Failed",
  WORKFLOW_CLEAR_ERROR = "[Workflow] Clear Error",
  DELETE_WORKFLOW = "[Workflow] Remove",
  DELETE_WORKFLOW_SUCCESSFUL = "[Workflow] Remove Successful",
}

export class GetWcmSystem implements Action {
  readonly type = WcmSystemActionTypes.GET_WCMSYSTEM;
  constructor(public payload: CurrentSite) {}
}

export class GetWcmSystemForAuthoring implements Action {
  readonly type = WcmSystemActionTypes.GET_WCMSYSTEM_FOR_AUTHORING;
  constructor(public payload: CurrentSite) {}
}

export class GetWcmSystemSuccess implements Action {
  readonly type = WcmSystemActionTypes.GET_WCMSYSTEM_SUCCESS;
  constructor(public payload: any) {}
}

export class GetWcmSystemFailed implements Action {
  readonly type = WcmSystemActionTypes.GET_WCMSYSTEM_FAILED;
  constructor(public payload: string) {}
}

export class UpdateSiteareaLayout implements Action {
  readonly type = WcmSystemActionTypes.UPDATE_SITE_AREA_LAYOUT;
  constructor(public payload: SiteArea) {}
}

export class WcmSystemClearError implements Action {
  readonly type = WcmSystemActionTypes.WCMSYSTEM_CLEAR_ERROR;
  constructor() {}
}

export class CreateRenderTemplate implements Action {
  readonly type = WcmSystemActionTypes.CREATE_RENDER_TEMPLATE;
  constructor(public payload: RenderTemplate) {}
}

export class UpdateRenderTemplate implements Action {
  readonly type = WcmSystemActionTypes.UPDATE_RENDER_TEMPLATE;
  constructor(public payload: RenderTemplate) {}
}

export class DeleteRenderTemplate implements Action {
  readonly type = WcmSystemActionTypes.DELETE_RENDER_TEMPLATE;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      wcmPath: string;
    }
  ) {}
}

export class DeleteRenderTemplateSuccessful implements Action {
  readonly type = WcmSystemActionTypes.DELETE_RENDER_TEMPLATE_SUCCESSFUL;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      library: string;
      name: string;
    }
  ) {}
}

export class RenderTemplateActionSuccessful implements Action {
  readonly type = WcmSystemActionTypes.RENDER_TEMPLATE_ACTION_SUCCESSFUL;
  constructor(public payload: any) {}
}

export class RenderTemplateActionFailed implements Action {
  readonly type = WcmSystemActionTypes.RENDER_TEMPLATE_ACTION_FAILED;
  constructor(public payload: string) {}
}

export class RenderTemplateClearError implements Action {
  readonly type = WcmSystemActionTypes.RENDER_TEMPLATE_CLEAR_ERROR;
  constructor() {}
}

export class CreateAuthoringTemplate implements Action {
  readonly type = WcmSystemActionTypes.CREATE_AUTHORING_TEMPLATE;
  constructor(public payload: AuthoringTemplate) {}
}

export class UpdateAuthoringTemplate implements Action {
  readonly type = WcmSystemActionTypes.UPDATE_AUTHORING_TEMPLATE;
  constructor(public payload: AuthoringTemplate) {}
}

export class DeleteAuthoringTemplate implements Action {
  readonly type = WcmSystemActionTypes.DELETE_AUTHORING_TEMPLATE;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      wcmPath: string;
    }
  ) {}
}

// export class CreateAuthoringTemplateSuccess implements Action {
//   readonly type = WcmSystemActionTypes.CREATE_AUTHORING_TEMPLATE_SUCCESS;
//   constructor(public payload: any) {
//   }
// }

export class AuthoringTemplateFailed implements Action {
  readonly type = WcmSystemActionTypes.AUTHORING_TEMPLATE_FAILED;
  constructor(public payload: string) {}
}

export class AuthoringTemplateSuccessful implements Action {
  readonly type = WcmSystemActionTypes.AUTHORING_TEMPLATE_SUCCESSFUL;
  constructor(public payload: AuthoringTemplate) {}
}

export class DeleteAuthoringTemplateSuccessful implements Action {
  readonly type = WcmSystemActionTypes.DELETE_AUTHORING_TEMPLATE_SUCCESSFUL;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      library: string;
      name: string;
    }
  ) {}
}

export class AuthoringTemplateClearError implements Action {
  readonly type = WcmSystemActionTypes.AUTHORING_TEMPLATE_CLEAR_ERROR;
  constructor() {}
}

export class CreateForm implements Action {
  readonly type = WcmSystemActionTypes.CREATE_FORM;
  constructor(public payload: Form) {}
}

export class UpdateForm implements Action {
  readonly type = WcmSystemActionTypes.UPDATE_FORM;
  constructor(public payload: Form) {}
}

export class DeleteForm implements Action {
  readonly type = WcmSystemActionTypes.DELETE_FORM;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      wcmPath: string;
    }
  ) {}
}

export class FormFailed implements Action {
  readonly type = WcmSystemActionTypes.FORM_FAILED;
  constructor(public payload: string) {}
}

export class FormSuccessful implements Action {
  readonly type = WcmSystemActionTypes.FORM_SUCCESSFUL;
  constructor(public payload: Form) {}
}

export class DeleteFormSuccessful implements Action {
  readonly type = WcmSystemActionTypes.DELETE_FORM_SUCCESSFUL;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      library: string;
      name: string;
    }
  ) {}
}

export class FormClearError implements Action {
  readonly type = WcmSystemActionTypes.FORM_CLEAR_ERROR;
  constructor() {}
}

export class CreateContentAreaLayout implements Action {
  readonly type = WcmSystemActionTypes.CREATE_CONTENT_AREA_LAYOUT;
  constructor(public payload: ContentAreaLayout) {}
}

export class UpdateContentAreaLayout implements Action {
  readonly type = WcmSystemActionTypes.UPDATE_CONTENT_AREA_LAYOUT;
  constructor(public payload: ContentAreaLayout) {}
}
export class EditContentAreaLayout implements Action {
  readonly type = WcmSystemActionTypes.EDIT_CONTENT_AREA_LAYOUT;
  constructor(public payload: ContentAreaLayout) {}
}

export class CreateNewContentAreaLayout implements Action {
  readonly type = WcmSystemActionTypes.CREATE_NEW_CONTENT_AREA_LAYOUT;
  constructor(
    public repository: string,
    public workspace: string,
    public library: string
  ) {}
}
export class ContentAreaLayoutSuccessful implements Action {
  readonly type = WcmSystemActionTypes.CONTENT_AREA_LAYOUT_SUCCESSFUL;
  constructor(public payload: ContentAreaLayout) {}
}

export class ContentAreaLayoutFailed implements Action {
  readonly type = WcmSystemActionTypes.CONTENT_AREA_LAYOUT_FAILED;
  constructor(public payload: string) {}
}

export class RemoveContentAreaLayout implements Action {
  readonly type = WcmSystemActionTypes.REMOVE_CONTENT_AREA_LAYOUT;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      wcmPath: string;
    }
  ) {}
}

export class RemoveContentAreaLayoutSuccessful implements Action {
  readonly type = WcmSystemActionTypes.REMOVE_CONTENT_AREA_LAYOUT_SUCCESSFUL;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      library: string;
      name: string;
    }
  ) {}
}

export class ContentAreaLayoutClearError implements Action {
  readonly type = WcmSystemActionTypes.CONTENT_AREA_LAYOUT_CLEAR_ERROR;
  constructor() {}
}

export type WcmSystemActions =
  | GetWcmSystem
  | GetWcmSystemForAuthoring
  | GetWcmSystemSuccess
  | GetWcmSystemFailed
  | UpdateSiteareaLayout
  | WcmSystemClearError
  | CreateRenderTemplate
  | UpdateRenderTemplate
  | RenderTemplateActionSuccessful
  | RenderTemplateActionFailed
  | DeleteRenderTemplate
  | DeleteRenderTemplateSuccessful
  | RenderTemplateClearError
  | CreateAuthoringTemplate
  | UpdateAuthoringTemplate
  | DeleteAuthoringTemplate
  | AuthoringTemplateSuccessful
  | AuthoringTemplateFailed
  | DeleteAuthoringTemplateSuccessful
  | AuthoringTemplateClearError
  | CreateForm
  | UpdateForm
  | DeleteForm
  | FormSuccessful
  | FormFailed
  | DeleteFormSuccessful
  | FormClearError
  | CreateContentAreaLayout
  | UpdateContentAreaLayout
  | CreateNewContentAreaLayout
  | EditContentAreaLayout
  | RemoveContentAreaLayout
  | ContentAreaLayoutSuccessful
  | ContentAreaLayoutFailed
  | RemoveContentAreaLayoutSuccessful
  | ContentAreaLayoutClearError;
