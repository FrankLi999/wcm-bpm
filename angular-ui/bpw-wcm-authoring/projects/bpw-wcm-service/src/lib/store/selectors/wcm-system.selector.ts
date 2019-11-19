import { createSelector } from '@ngrx/store';
import { WcmSystemState } from '../reducers/wcm-system.reducer';
import { getWcmAppState, WcmAppState } from '../reducers/wcm-authoring.reducer';
import { WcmSystem } from '../../model/WcmSystem';
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

export const getRenderTemplates= createSelector(
    getWcmSystem,
    (wcmSystem: WcmSystem) => wcmSystem.renderTemplates
);

export const getContentAreaLayouts = createSelector(
    getWcmSystem,
    (wcmSystem: WcmSystem) => wcmSystem.contentAreaLayouts
);

export const getThemes= createSelector(
    getWcmSystem,
    (wcmSystem: WcmSystem) => wcmSystem.jcrThemes
);

export const getWcmRepositories= createSelector(
    getWcmSystem,
    (wcmSystem: WcmSystem) => wcmSystem.wcmRepositories
);

export const getJsonForms = createSelector(
    getWcmSystem,
    (wcmSystem: WcmSystem) => wcmSystem.jsonForms
);

export const getAuthoringTemplates = createSelector(
    getWcmSystem,
    (wcmSystem: WcmSystem) => wcmSystem.authoringTemplates
);

export const getOperations = createSelector(
    getWcmSystem,
    (wcmSystem: WcmSystem) => wcmSystem.operations
);

export const getControlFiels = createSelector(
    getWcmSystem,
    (wcmSystem: WcmSystem) => wcmSystem.controlFiels
);

export const getCreateAuthoringTemplateError = createSelector(
    getWcmSystemState,
    (state: WcmSystemState) => state.atError
);

export const getCreateRenderTemplateError = createSelector(
    getWcmSystemState,
    (state: WcmSystemState) => state.rtError
);

export const getGetWcmSystemError = createSelector(
    getWcmSystemState,
    (state: WcmSystemState) => state.loadError
);