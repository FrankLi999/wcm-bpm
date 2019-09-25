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
  import { AuthService } from './auth.service'
  import { UserProfile } from '../model/user-profile.model';
  import * as fromStore from '../store';

  @Injectable()
  export class AuthHttpInterceptor implements HttpInterceptor {
    constructor(
      private authService: AuthService, 
      private store: Store<fromStore.AuthState>,
      private router: Router) {}
      
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      return this.store.pipe(
        select(fromStore.getUserProfile),
        take(1),
        flatMap((userProfile: UserProfile) => {
              const authRequest = userProfile.accessToken ? 
                  req.clone({ setHeaders: { authorization: `${userProfile.tokenType} ${userProfile.accessToken}` } }) :
                  req;
              return next.handle(authRequest);
        }),
        catchError((err, caught) => {
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