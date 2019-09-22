import { createSelector } from '@ngrx/store';
import { 
  AppConfigurationState,
  getAppState,
  NavigationState
} from '../reducers/navigation.reducers';

//getAppConfiguration
export const getAppConfigurationState = createSelector(
    getAppState,
    (state: AppConfigurationState) => state
);

export const getNavigationState = createSelector(
    getAppConfigurationState,
    (appState: AppConfigurationState) => appState.navigation
);

export const getNavigation = createSelector(
    getNavigationState,
    (navState: NavigationState) => navState.navigation
);