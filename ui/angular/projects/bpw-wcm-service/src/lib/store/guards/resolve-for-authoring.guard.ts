import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate } from "@angular/router";
import { RouterStateSnapshot, Router } from "@angular/router";
import { select, Store } from "@ngrx/store";

import { Observable, of } from "rxjs";
import { switchMap, catchError, tap, take, filter } from "rxjs/operators";

import { WcmAppState } from "../reducers/wcm-authoring.reducer";
import { GetWcmSystemForAuthoring } from "../actions/wcm-system.actions";
import { getWcmSystemLoaded } from "../selectors/wcm-system.selector";
import { WcmConstants } from "../../utils/wcm-constants";
import { AuthState, getRouteState, RouteSnapshot, UiService } from "bpw-common";
import * as authStore from "bpw-common";

import { appConfig } from "bpw-common";
// declare var appConfig: any;
@Injectable({
  providedIn: "root",
})
export class ResolveForAuthoringGuard implements CanActivate {
  routerState: RouteSnapshot;

  /**
   * Constructor
   *
   * @param Store<WcmAppState> _store
   */
  constructor(
    private _authStore: Store<AuthState>,
    private _store: Store<WcmAppState>,
    private _uiService: UiService
  ) {
    this._store.pipe(select(getRouteState)).subscribe((routerState) => {
      if (routerState) {
        this.routerState = routerState.state;
      }
    });
  }

  /**
   * Can activate
   *
   * @param ActivatedRouteSnapshot route
   * @param RouterStateSnapshot state
   * @returns Observable<boolean>
   */
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> {
    return this.checkStore().pipe(
      switchMap(() => of(true)),
      catchError((error) => {
        this._uiService.gotoApplicationErrorPage(
          "Failed to load site configurations"
        );
        return of(false);
      })
    );
  }

  /**
   * Check store
   *
   * @returns Observable<any>
   */
  checkStore(): Observable<any> {
    return this._authStore.pipe(
      select(authStore.isLoggedIn),
      take(1),
      switchMap((loggedIn) => {
        if (!(loggedIn || this._uiService.getUserProfile())) {
          return of(false);
        } else {
          return this.getWcmSystemForAuthoring().pipe(
            filter((wcmSystemLoaded) => wcmSystemLoaded),
            take(1)
          );
        }
      })
    );
  }

  /**
   * Get WcmSystem
   *
   * @returns Observable<any>
   */
  getWcmSystemForAuthoring(): Observable<any> {
    return this._store.pipe(
      select(getWcmSystemLoaded),
      tap((loaded) => {
        if (!loaded) {
          this._store.dispatch(
            new GetWcmSystemForAuthoring({
              repository: WcmConstants.REPO_BPWIZARD,
              workspace: WcmConstants.WS_DEFAULT,
              library: appConfig.library,
              siteConfig: appConfig.siteConfig,
            })
          );
        }
      }),
      filter((loaded) => loaded),
      take(1)
    );
  }
}
