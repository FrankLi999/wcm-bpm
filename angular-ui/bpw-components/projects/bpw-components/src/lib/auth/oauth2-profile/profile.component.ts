import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { FuseConfigService } from '../../common/services/config.service';
import { fuseAnimations } from '../../common/animations';
import { authLayoutConfig } from '../config/auth.config';
import * as fromStore from 'bpw-auth-store';

@Component({
  selector: 'user-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations
})
export class ProfileComponent implements OnInit, OnDestroy {
  private unsubscribeAll: Subject<any>;
  userProfile: fromStore.UserProfile;

  constructor(
    private _fuseConfigService: FuseConfigService,
    private store: Store<fromStore.AuthState>,
    private router: Router) { 
      this.unsubscribeAll = new Subject<any>();
      // Configure the layout
      this._fuseConfigService.config = {
        ...authLayoutConfig
    };
  }

  /**
  * On init
  */
  ngOnInit(): void {
    this.store.pipe(select(fromStore.getUserProfile), takeUntil(this.unsubscribeAll)).subscribe(
      (userProfile: fromStore.UserProfile) => {
        this.userProfile = userProfile;
      }
    );
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
  }

  handleLogout() {
    this.store.dispatch(new fromStore.LogoutAction());
      this.store.pipe(
          select(fromStore.isLoggedOut),
          take(1))
        .subscribe(
          (loggedOut: boolean) => {
            this.router.navigateByUrl('/auth/login');
          }
      );
  }
}
