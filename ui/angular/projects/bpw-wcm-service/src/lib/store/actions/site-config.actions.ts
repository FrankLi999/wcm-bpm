import { Action } from "@ngrx/store";
import { SiteConfig } from "../../model/SiteConfig";
import { LoadParameters } from "../../model/load-parameters";

export enum SiteConfigActionTypes {
  LOAD_SITE_CONFIG = "[SiteConfig] LOAD",
  LOAD_SITE_CONFIG_SUCCESSFUL = "[SiteConfig] LOAD Successful",
  LOAD_SITE_CONFIG_FAILED = "[SiteConfig] LOAD FAILED",
  CREATE_SITE_CONFIG = "[SiteConfig] Create",
  UPDATE_SITE_CONFIG = "[SiteConfig] Update",
  CREATE_SITE_CONFIG_SUCCESSFUL = "[SiteConfig] Create Successful",
  UPDATE_SITE_CONFIG_SUCCESSFUL = "[SiteConfig] Update Successful",
  SITE_CONFIG_ACTION_FAILED = "[SiteConfig] Action Failed",
  SITE_CONFIG_CLEAR_ERROR = "[SiteConfig] Clear Error",
  DELETE_SITE_CONFIG = "[SiteConfig] Remove",
  DELETE_SITE_CONFIG_SUCCESSFUL = "[SiteConfig] Remove Successful"
}

export class LoadSiteConfig implements Action {
  readonly type = SiteConfigActionTypes.LOAD_SITE_CONFIG;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      library: string;
    }
  ) {}
}

export class LoadSiteConfigSuccessful implements Action {
  readonly type = SiteConfigActionTypes.LOAD_SITE_CONFIG_SUCCESSFUL;
  constructor(public payload: SiteConfig[]) {}
}

export class LoadSiteConfigFailed implements Action {
  readonly type = SiteConfigActionTypes.LOAD_SITE_CONFIG_FAILED;
  constructor(public payload: string) {}
}

export class CreateSiteConfig implements Action {
  readonly type = SiteConfigActionTypes.CREATE_SITE_CONFIG;
  constructor(public payload: SiteConfig) {}
}

export class UpdateSiteConfig implements Action {
  readonly type = SiteConfigActionTypes.UPDATE_SITE_CONFIG;
  constructor(public payload: SiteConfig) {}
}

export class CreateSiteConfigSuccessful implements Action {
  readonly type = SiteConfigActionTypes.CREATE_SITE_CONFIG_SUCCESSFUL;
  constructor(public payload: SiteConfig) {}
}

export class UpdateSiteConfigSuccessful implements Action {
  readonly type = SiteConfigActionTypes.UPDATE_SITE_CONFIG_SUCCESSFUL;
  constructor(public payload: SiteConfig) {}
}

export class SiteConfigActionFailed implements Action {
  readonly type = SiteConfigActionTypes.SITE_CONFIG_ACTION_FAILED;
  constructor(public payload: string) {}
}

export class DeleteSiteConfig implements Action {
  readonly type = SiteConfigActionTypes.DELETE_SITE_CONFIG;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      wcmPath: string;
    }
  ) {}
}

export class DeleteSiteConfigSuccessful implements Action {
  readonly type = SiteConfigActionTypes.DELETE_SITE_CONFIG_SUCCESSFUL;
  constructor(
    public payload: {
      repository: string;
      workspace: string;
      library: string;
      name: string;
    }
  ) {}
}

export class SiteConfigClearError implements Action {
  readonly type = SiteConfigActionTypes.SITE_CONFIG_CLEAR_ERROR;
  constructor() {}
}

export type SiteConfigActions =
  | LoadSiteConfig
  | LoadSiteConfigSuccessful
  | LoadSiteConfigFailed
  | CreateSiteConfig
  | UpdateSiteConfig
  | DeleteSiteConfig
  | CreateSiteConfigSuccessful
  | UpdateSiteConfigSuccessful
  | SiteConfigActionFailed
  | DeleteSiteConfigSuccessful
  | SiteConfigClearError;
