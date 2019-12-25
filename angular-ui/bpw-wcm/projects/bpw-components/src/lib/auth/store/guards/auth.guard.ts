import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateChild,
  CanLoad,
  Router,
  Route,
  RouterStateSnapshot
} from '@angular/router';
import { Observable } from 'rxjs';
import { select, Store } from '@ngrx/store';
import { AuthState } from '../reducers/auth.reducers';
import { isLoggedIn, getUserProfile } from '../selectors/auth.selectors';
import { map, mergeMap } from 'rxjs/operators';
import { iif, of } from 'rxjs';
import { UserProfile } from '../model/user-profile.model';
import { UiService } from '../service/ui.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild, CanLoad {


  constructor(
    private store: Store<AuthState>, 
    private uiService: UiService,
    private router: Router) {
  }

  canLoad(route: Route): boolean | Observable<boolean> | Promise<boolean> {
    return this.store
      .pipe(
        select(isLoggedIn)
      );
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | Observable<boolean> | Promise<boolean> {
    return this.checkLogin(route, state)
  }

  canActivateChild(
    childRoute: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | Observable<boolean> | Promise<boolean> {
    return this.checkLogin(childRoute, state)
  }

  protected checkLogin(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.store
      .pipe(
        select(isLoggedIn),
        mergeMap(
          loggedIn => iif(() => loggedIn, 
          this.store.select(getUserProfile), of(false))),
        map((loginStatus: UserProfile|boolean) => {
          let needLogin = false;
          let needRoleMatch = false;
          if (typeof loginStatus === 'boolean' ) {
            needLogin = true;
          } else {
            const expectedRoles: string[] = route.data.expectedRoles;
            const roles: string[] = (loginStatus as UserProfile).roles || [];
            if (expectedRoles) {
              needRoleMatch = roles.filter(role => expectedRoles.indexOf(role) >= 0).length === 0;
            }
          }
          if (needLogin || needRoleMatch) {
              this.showAlert(needLogin, needRoleMatch)
              this.router.navigateByUrl(`/auth/login?url=${state.url}`);
              return false;
          }
          return true;
        })
    );
    
  }

  private showAlert(isAuth: boolean, roleMatch: boolean) {
    if (!isAuth) {
      this.uiService.showToast('You must login to continue')
    }

    if (!roleMatch) {
      this.uiService.showToast('You do not have the permissions to view this resource')
    }
  }
}
