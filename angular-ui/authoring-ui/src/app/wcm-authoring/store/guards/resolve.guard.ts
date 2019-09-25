import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate } from '@angular/router';
import { RouterStateSnapshot } from '@angular/router';
import { select, Store } from '@ngrx/store';

import { Observable, of } from 'rxjs';
import { switchMap, catchError, tap, take, filter } from 'rxjs/operators';

import { WcmAppState } from '../reducers';
import * as fromStore from '../';
import { getWcmSystemLoaded } from '../selectors';
import { getRouteState, RouteSnapshot } from 'bpw-components';
@Injectable()
export class ResolveGuard implements CanActivate {
    routerState: RouteSnapshot;

    /**
     * Constructor
     *
     * @param {Store<WcmAppState>} _store
     */
    constructor(
        private _store: Store<WcmAppState>
    ) {
        this._store
            .pipe(select(getRouteState))
            .subscribe(routerState => {
                if ( routerState ) {
                    this.routerState = routerState.state;
                }
            });
    }

    /**
     * Can activate
     *
     * @param {ActivatedRouteSnapshot} route
     * @param {RouterStateSnapshot} state
     * @returns {Observable<boolean>}
     */
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.checkStore().pipe(
            switchMap(() => of(true)),
            catchError(() => of(false))
        );
    }

    /**
     * Check store
     *
     * @returns {Observable<any>}
     */
    checkStore(): Observable<any> {
        return this.getWcmSystem().pipe(
            filter(wcmSystemLoaded => wcmSystemLoaded),
            take(1)
        );
    }

    /**
     * Get WcmSystem
     *
     * @returns {Observable<any>}
     */
    getWcmSystem(): Observable<any> {
        return this._store.pipe(
            select(getWcmSystemLoaded),
            tap(loaded => {
                if ( !loaded )
                {
                    this._store.dispatch(new fromStore.GetWcmSystem({
                        repository: 'bpwizard',
                        workspace: 'default',
                        library: 'camunda',
                        siteConfig: 'bpm'
                    }));
                }
            }),
            filter(loaded => loaded),
            take(1)
        );
    }
}
