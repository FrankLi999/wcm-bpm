import { NavigationActionTypes, NavigationActions } from '../actions/navigation.action';
import { Navigation } from '../../../common/types/navigation';
import { ActionReducerMap, createFeatureSelector } from '@ngrx/store';

export interface NavigationState {
    navigation?: Navigation[],
    error?: string;
}

export const NavigationStateInitialState: NavigationState = {
    error: null,
    navigation: null
};

export function NavigationReducer(state = NavigationStateInitialState, action: NavigationActions): NavigationState {
    switch (action.type) {
        case NavigationActionTypes.SET_WCM_NAVIGATION:

            return {
                navigation: [...action.payload],
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