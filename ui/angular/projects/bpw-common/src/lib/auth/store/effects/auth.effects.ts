import { Injectable } from "@angular/core";
import { Actions, Effect, ofType } from "@ngrx/effects";
import { catchError, filter, map, switchMap, tap } from "rxjs/operators";
import { of } from "rxjs";
import {
  AuthActions,
  AuthActionTypes,
  LoginAction,
  LoginSucceedAction,
  LoginFailedAction,
} from "../actions/auth.actions";
import { AuthService } from "../service/auth.service";
import { UiService } from "../service/ui.service";
import { UserProfile } from "../model/user-profile.model";

@Injectable()
export class AuthEffects {
  @Effect({ dispatch: true })
  login$ = this.actions$.pipe(
    ofType<LoginAction>(AuthActionTypes.LoginAction),
    switchMap((action) => {
      return this.authService.login(action.payload).pipe(
        filter((userProfile) => !!userProfile),
        tap((userProfile) => {
          this.uiService.saveUserProfile(userProfile);
        }),
        map((userProfile: UserProfile) => {
          return new LoginSucceedAction(userProfile);
        }),
        catchError((err) => of(new LoginFailedAction(err)))
      );
    })
  );

  constructor(
    private actions$: Actions<AuthActions>,
    private authService: AuthService,
    private uiService: UiService
  ) {}
}
