import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import * as fromStore from 'bpw-store';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy {
  private unsubscribeAll: Subject<any>;
  userProfile: fromStore.UserProfile;

  constructor(
    private store: Store<fromStore.AuthState>,
    private router: Router) { 
      this.unsubscribeAll = new Subject<any>();
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
