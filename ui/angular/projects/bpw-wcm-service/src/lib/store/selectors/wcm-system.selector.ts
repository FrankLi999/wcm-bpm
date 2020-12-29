import { createSelector } from "@ngrx/store";
import { WcmSystemState } from "../reducers/wcm-system.reducer";
import { getWcmAppState, WcmAppState } from "../reducers/wcm-authoring.reducer";
import { WcmSystem } from "../../model/WcmSystem";
import { WcmConstants } from "../../utils/wcm-constants";
import { WcmUtils } from "../../utils/wcm-utils";

export const getWcmSystemState = createSelector(
  getWcmAppState,
  (state: WcmAppState) => state.wcmSystem
);

export const getWcmSystem = createSelector(
  getWcmSystemState,
  (state: WcmSystemState) => state.wcmSystem
);

export const getWcmSystemLoaded = createSelector(
  getWcmSystemState,
  (state: WcmSystemState) => state.loaded
);

export const getRenderTemplates = createSelector(
  getWcmSystem,
  (wcmSystem: WcmSystem) => wcmSystem.renderTemplates
);

export const getRenderTemplateByLibraryAndName = createSelector(
  getRenderTemplates,
  (renderTemplates, props) =>
    renderTemplates[
      WcmUtils.itemPath(props.library, WcmConstants.ROOTNODE_RT, props.name)
    ]
);

export const getContentAreaLayouts = createSelector(
  getWcmSystem,
  (wcmSystem: WcmSystem) => wcmSystem.contentAreaLayouts
);

export const getThemes = createSelector(
  getWcmSystem,
  (wcmSystem: WcmSystem) => wcmSystem.jcrThemes
);

export const getWcmRepositories = createSelector(
  getWcmSystem,
  (wcmSystem: WcmSystem) => wcmSystem.wcmRepositories
);

export const getAuthoringTemplateForms = createSelector(
  getWcmSystem,
  (wcmSystem: WcmSystem) => wcmSystem.authoringTemplateForms
);

export const getForms = createSelector(
  getWcmSystem,
  (wcmSystem: WcmSystem) => wcmSystem.forms
);

export const getFormTemplates = createSelector(
  getWcmSystem,
  (wcmSystem: WcmSystem) => wcmSystem.formTemplates
);

export const getAuthoringTemplates = createSelector(
  getWcmSystem,
  (wcmSystem: WcmSystem) => wcmSystem.authoringTemplates
);

export const getAuthoringTemplateByLibraryAndName = createSelector(
  getAuthoringTemplates,
  (authoringTemplates, props) =>
    authoringTemplates[
      WcmUtils.itemPath(props.library, WcmConstants.ROOTNODE_AT, props.name)
    ]
);

export const getFormTemplateByLibraryAndName = createSelector(
  getFormTemplates,
  (formTemplates, props) =>
    formTemplates[
      WcmUtils.itemPath(props.library, WcmConstants.ROOTNODE_FORM, props.name)
    ]
);

export const getOperations = createSelector(
  getWcmSystem,
  (wcmSystem: WcmSystem) => wcmSystem.operations
);

export const getQueries = createSelector(
  getWcmSystem,
  (wcmSystem: WcmSystem) => wcmSystem.queryStatements
);

export const getControlFiels = createSelector(
  getWcmSystem,
  (wcmSystem: WcmSystem) => wcmSystem.controlFiels
);

export const getAuthoringTemplateError = createSelector(
  getWcmSystemState,
  (state: WcmSystemState) => state.atError
);

export const getFormError = createSelector(
  getWcmSystemState,
  (state: WcmSystemState) => state.formError
);

export const getRenderTemplateError = createSelector(
  getWcmSystemState,
  (state: WcmSystemState) => state.rtError
);

export const getContentAreaLayoutsError = createSelector(
  getWcmSystemState,
  (state: WcmSystemState) => state.renderError
);

export const getGetWcmSystemError = createSelector(
  getWcmSystemState,
  (state: WcmSystemState) => state.loadError
);
