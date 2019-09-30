import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Store } from '@ngrx/store';
import * as fromStore from 'bpw-store';
import { API_BASE_URL, ACCESS_TOKEN} from 'bpw-rest-client';

@Component({
  selector: 'redirect-handler',
  templateUrl: './redirect-handler.component.html',
  styleUrls: ['./redirect-handler.component.scss']
})
export class RedirectHandlerComponent implements OnInit {
  constructor(
    private store: Store<fromStore.AuthState>,
    private http: HttpClient,
    private router: Router, 
    private route: ActivatedRoute) { }

  ngOnInit() {
    const token = this.route.snapshot.queryParams['token'];
    const error = this.route.snapshot.queryParams['error'];

    if (token) {
      this.store.dispatch(new fromStore.LoginSucceedAction(
        {
          id: 'oauth2-user',
          email: '',
          accessToken: token,
          roles: [],
          tokenType: 'Bearer'
        },
      ));
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      this.http.get(`${API_BASE_URL}/user/api/rest/me`, {headers}).subscribe(
        (userProfile: fromStore.UserProfile) => {
          userProfile.accessToken = token;
          this.store.dispatch(new fromStore.LoginSucceedAction(userProfile));
        },
        response => {
          console.log("get user profile call ended in error", response);
          console.log(response);
        },
        () => {
          console.log("get user profile observable is now completed.");
        }
      );
      this.router.navigateByUrl('/oauth2/profile');
    } else {
      //uiService.showError
      this.router.navigateByUrl('/auth/login');    
    }
  }
}