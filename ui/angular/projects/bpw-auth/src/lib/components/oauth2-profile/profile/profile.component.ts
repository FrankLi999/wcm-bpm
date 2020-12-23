import { Component, OnInit, OnDestroy, ViewEncapsulation } from "@angular/core";
import { Router } from "@angular/router";
import { select, Store } from "@ngrx/store";
import { Subject } from "rxjs";
import { take, takeUntil } from "rxjs/operators";
import {
  authLayoutConfig,
  UiService,
  UIConfigService,
  UserProfile,
  wcmAnimations,
} from "bpw-common";

import * as authStore from "bpw-common";

@Component({
  selector: "user-profile",
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ProfileComponent implements OnInit, OnDestroy {
  private unsubscribeAll: Subject<any>;
  userProfile: UserProfile;

  constructor(
    private _router: Router,
    private _store: Store<authStore.AuthState>,
    private _uiConfigService: UIConfigService,
    private _uiService: UiService
  ) {
    this.unsubscribeAll = new Subject<any>();
    // Configure the layout
    this._uiConfigService.config = {
      ...authLayoutConfig,
    };
  }

  /**
   * On init
   */
  ngOnInit(): void {
    this._store
      .pipe(select(authStore.getUserProfile), takeUntil(this.unsubscribeAll))
      .subscribe((userProfile: UserProfile) => {
        this.userProfile = userProfile;
        if (!this.userProfile) {
          this.userProfile = this._uiService.getUserProfile();
        }
      });
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
  }

  handleLogout() {
    this._store.dispatch(new authStore.LogoutAction());
    this._store
      .pipe(select(authStore.isLoggedOut), take(1))
      .subscribe((loggedOut: boolean) => {
        this._router.navigateByUrl("/auth/login");
      });
  }
}
