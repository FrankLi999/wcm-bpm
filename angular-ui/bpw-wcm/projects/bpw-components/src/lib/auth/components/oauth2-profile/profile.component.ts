import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { Subject } from 'rxjs';
import { take, takeUntil } from 'rxjs/operators';
import { UIConfigService } from '../../../common/services/config.service';
import { wcmAnimations } from '../../../common/animations/wcm-animations';
import { authLayoutConfig } from '../../config/auth.config';
import * as authStoreSelector from '../../store/selectors/auth.selectors';
import * as authStoreAction from '../../store/actions/auth.actions';
import * as authStoreReducer from '../../store/reducers/auth.reducers';
import { UserProfile } from '../../store/model/user-profile.model';
@Component({
  selector: 'user-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : wcmAnimations
})
export class ProfileComponent implements OnInit, OnDestroy {
  private unsubscribeAll: Subject<any>;
  userProfile: UserProfile;

  constructor(
    private _uiConfigService: UIConfigService,
    private store: Store<authStoreReducer.AuthState>,
    private router: Router) { 
      this.unsubscribeAll = new Subject<any>();
      // Configure the layout
      this._uiConfigService.config = {
        ...authLayoutConfig
    };
  }

  /**
  * On init
  */
  ngOnInit(): void {
    this.store.pipe(select(authStoreSelector.getUserProfile), takeUntil(this.unsubscribeAll)).subscribe(
      (userProfile: UserProfile) => {
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
    this.store.dispatch(new authStoreAction.LogoutAction());
      this.store.pipe(
          select(authStoreSelector.isLoggedOut),
          take(1))
        .subscribe(
          (loggedOut: boolean) => {
            this.router.navigateByUrl('/auth/login');
          }
      );
  }
}
