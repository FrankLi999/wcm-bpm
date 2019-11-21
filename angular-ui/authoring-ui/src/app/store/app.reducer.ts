import { ActionReducerMap } from '@ngrx/store';
import { routerReducer, RouterReducerState } from '@ngrx/router-store';
import { RouteSnapshot } from 'bpw-auth-store';

export interface AppState {
    routeState: RouterReducerState<RouteSnapshot>
}

export const appReducers: ActionReducerMap<AppState> = {
    routeState: routerReducer
};