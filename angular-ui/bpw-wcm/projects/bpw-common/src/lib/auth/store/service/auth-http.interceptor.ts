import {
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest,
  } from '@angular/common/http'
  import { Injectable } from '@angular/core'
  import { Router } from '@angular/router'
  import { Observable, throwError as observableThrowError } from 'rxjs'
  import { catchError, take, flatMap, filter } from 'rxjs/operators'
  import { select, Store } from '@ngrx/store';
  import { UserProfile } from '../model/user-profile.model';
  import { AuthState } from '../reducers/auth.reducers';
  import { getUserProfile } from '../selectors/auth.selectors';
  // import * as fromStore from '../store';
  
  @Injectable({ 
    providedIn: 'root' 
  })
  export class AuthHttpInterceptor implements HttpInterceptor {
    constructor(
      // private authService: AuthService, 
      private store: Store<AuthState>,
      private router: Router) {}
      
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      return this.store.pipe(
        select(getUserProfile),
        take(1),
        flatMap((userProfile: UserProfile) => {
              const authRequest = userProfile.accessToken ? 
                  req.clone({ setHeaders: { authorization: `${userProfile.tokenType} ${userProfile.accessToken}` } }) :
                  req;
              return next.handle(authRequest);
        }),
        catchError((err) => {
          if (err.status === 401) {
            this.router.navigate(['/auth/login'], {
              queryParams: { redirectUrl: this.router.routerState.snapshot.url },
            })
          }
  
          return observableThrowError(err)
        })
      );
    }
}