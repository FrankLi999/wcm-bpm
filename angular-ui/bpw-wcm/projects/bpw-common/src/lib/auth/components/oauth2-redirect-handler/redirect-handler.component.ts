import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Store } from '@ngrx/store';

import * as authStoreAction from '../../store/actions/auth.actions';
import * as authStoreReducer from '../../store/reducers/auth.reducers';
import { UserProfile } from '../../store/model/user-profile.model';

import { ApiConfigService } from 'bpw-rest-client';

declare var appConfig: any;

@Component({
  selector: 'redirect-handler',
  templateUrl: './redirect-handler.component.html',
  styleUrls: ['./redirect-handler.component.scss']
})
export class RedirectHandlerComponent implements OnInit {
  constructor(
    private store: Store<authStoreReducer.AuthState>,
    private http: HttpClient,
    private apiConfigService: ApiConfigService,
    private router: Router, 
    private route: ActivatedRoute) { }

  ngOnInit() {
    const token = this.route.snapshot.queryParams['token'];
    const error = this.route.snapshot.queryParams['error'];

    if (token) {
      this.store.dispatch(new authStoreAction.LoginSucceedAction(
        {
          id: 'oauth2-user',
          email: '',
          accessToken: token,
          roles: [],
          tokenType: 'Bearer'
        },
      ));
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      this.http.get(`${this.apiConfigService.apiConfig.apiBaseUrl}/user/api/me`, {headers}).subscribe(
        (userProfile: UserProfile) => {
          userProfile.accessToken = token;
          this.store.dispatch(new authStoreAction.LoginSucceedAction(userProfile));
        },
        response => {
          console.log("get user profile call ended in error", response);
          console.log(response);
        },
        () => {
          console.log("get user profile observable is now completed.");
        }
      );
      // this.router.navigateByUrl('/oauth2/profile');
      // this.router.navigateByUrl('/wcm-authoring/jcr-explorer');
      this.router.navigateByUrl(appConfig && appConfig.defaultUrl ? appConfig.defaultUrl : '/oauth2/profile');
    } else {
      //uiService.showError
      this.router.navigateByUrl('/auth/login');    
    }
  }
}