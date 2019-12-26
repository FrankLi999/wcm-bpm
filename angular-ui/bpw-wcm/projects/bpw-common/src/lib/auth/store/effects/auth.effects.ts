import { Injectable } from '@angular/core';
import { Actions, Effect, ofType } from '@ngrx/effects';

import { switchMap, map, catchError, filter} from 'rxjs/operators';
import { of} from 'rxjs';
import { 
  AuthActions,
  AuthActionTypes,
  LoginAction,
  LoginSucceedAction,
  LoginFailedAction
} from '../actions/auth.actions';
import { AuthService } from '../service/auth.service';
import { UserProfile } from '../model/user-profile.model';

@Injectable()
export class AuthEffects {

  @Effect({dispatch:true})
  login$ = this.actions$.pipe(
    ofType<LoginAction>(AuthActionTypes.LoginAction),
    switchMap((action) => {
      return this.authService.login(action.payload)
        .pipe(
            filter(userProfile => !!userProfile),
            map((userProfile: UserProfile) => {
              return new LoginSucceedAction(userProfile);
            }),
            catchError(err => of(new LoginFailedAction(err)))
        );
      })
  );

  // @Effect({dispatch:true})
  // logout$ = this.actions$.pipe(
  //   ofType<LogoutAction>(AuthActionTypes.LogoutAction),
  //   switchMap((action) => {
  //     return this.authService.logout()
  //       .pipe(
  //           map(() => {
  //             return new LogoutSucceedAction();
  //           }),
  //           catchError(err => of(new LogoutFailedAction(err)))
  //       );
  //     })
  // );

  constructor(
    private actions$: Actions<AuthActions>,
    private authService: AuthService) {}
}
