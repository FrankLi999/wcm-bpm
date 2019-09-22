import { NavigationActionTypes, NavigationActions } from '../actions/navigation.action';
import { FuseNavigation } from 'bpw-components';
import { ActionReducerMap, createFeatureSelector } from '@ngrx/store';

export interface NavigationState {
    navigation?: FuseNavigation,
    error?: string;
}

export const NavigationStateInitialState: NavigationState = {
    error: null,
    navigation: null
};

export function NavigationReducer(state = NavigationStateInitialState, action: NavigationActions): NavigationState {
    switch (action.type) {
        case NavigationActionTypes.SET_FUSE_NAVIGATION:

            return {
                navigation: {...action.payload},
                error: null
            };
        default:
            return state;
    }
}

export const appFeatureKey = 'appConfig';

export interface AppConfigurationState {
    navigation: NavigationState;
}

export const reducers: ActionReducerMap<AppConfigurationState> = {
    navigation  : NavigationReducer
};

export const getAppState = createFeatureSelector<AppConfigurationState>(
    appFeatureKey
);