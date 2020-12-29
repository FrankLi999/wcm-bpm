import { createSelector } from "@ngrx/store";
import { getWcmAppState, WcmAppState } from "../reducers/wcm-authoring.reducer";
import {
  SiteConfigState,
  SiteConfigAdapter
} from "../reducers/site-config.reducer";
export const {
  selectIds: _selectSiteConfigIds,
  selectEntities: _selectSiteConfigEntitie,
  selectAll: _selectAllSiteConfigs,
  selectTotal: _selectSiteConfigTotal
} = SiteConfigAdapter.getSelectors();

export const getSiteConfigState = createSelector(
  getWcmAppState,
  (state: WcmAppState) => state.siteConfigState
);

export const getSiteConfigStatus = createSelector(
  getSiteConfigState,
  (state: SiteConfigState): string => state.status
);

export const getSiteConfigLoading = createSelector(
  getSiteConfigState,
  (state: SiteConfigState): boolean => state.loading
);

export const getSiteConfigTotal = createSelector(
  getSiteConfigState,
  _selectSiteConfigTotal
);

export const getSiteConfigs = createSelector(
  getSiteConfigState,
  _selectAllSiteConfigs
);

export const getSiteConfigEntities = createSelector(
  getSiteConfigState,
  _selectSiteConfigEntitie
);

export const getSiteConfigByLibraryAndName = createSelector(
  getSiteConfigEntities,
  (entities, props) => entities[`${props.library}_${props.name}`]
);
