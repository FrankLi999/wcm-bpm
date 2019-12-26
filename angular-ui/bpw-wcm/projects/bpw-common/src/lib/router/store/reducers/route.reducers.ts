import { ActivatedRouteSnapshot, RouterStateSnapshot, Data, ParamMap } from '@angular/router';
import { createSelector, ActionReducerMap } from '@ngrx/store';
import { BaseRouterStoreState, RouterReducerState, routerReducer, RouterStateSerializer} from '@ngrx/router-store';

///jcr-explorer/bpw/default?a=b#top
export interface RouteSnapshot extends BaseRouterStoreState {
    queryParamMap: ParamMap; //request parameter
    paramMap: ParamMap; //path variables
    data: Data;
    fragment: string; // the hash part on the url, top
}

export interface RouteSnapshotReducerState {
    routeState: RouterReducerState<RouteSnapshot>;
}

export const routeReducers: ActionReducerMap<RouteSnapshotReducerState> = {
    routeState: routerReducer
};

// export const getRouteState = createFeatureSelector<RouterReducerState<RouteSnapshot>>('routeState');
export const getRouteState = state => state.routeState;
export const getRouteSnapshot = createSelector(getRouteState, routeState => routeState.state);

export class RouteSnapshotSerializer implements RouterStateSerializer<RouteSnapshot> {
    serialize(routerState: RouterStateSnapshot): RouteSnapshot {
        const {
            url,
            root: { queryParamMap }
          } = routerState;

        let route: ActivatedRouteSnapshot = routerState.root;
        while ( route.firstChild ) {
            route = route.firstChild;
        }
        const {paramMap, data, fragment} = route;
        return {
            url,
            queryParamMap,
            paramMap,
            data,
            fragment 
        };
    }
}