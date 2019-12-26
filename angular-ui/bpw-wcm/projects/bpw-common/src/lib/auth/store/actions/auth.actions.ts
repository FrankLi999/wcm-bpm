import { Action } from '@ngrx/store';
import { Login } from '../model/login.model';
import {UserProfile} from '../model/user-profile.model';


export enum AuthActionTypes {
  LoginAction              = '[Login] Action',
  LoginAction_Succeed      = '[Login] Action Succeed',
  LoginAction_Failed       = '[Login] Action Failed',
  LoginAction_Clear_Error  = '[Login] Clear Error',
  LogoutAction             = '[Logout] Action'
}


export class LoginAction implements Action {

  readonly type = AuthActionTypes.LoginAction;

  constructor(public payload: Login) {

  }
}

export class LoginSucceedAction implements Action {

  readonly type = AuthActionTypes.LoginAction_Succeed;

  constructor(public payload: UserProfile) {

 }
}


export class LoginFailedAction implements Action {

  readonly type = AuthActionTypes.LoginAction_Failed;

  constructor(public payload: string) {

 }
}

export class LogoutAction implements Action {

  readonly type = AuthActionTypes.LogoutAction;

}

export class LoginClearErrorAction implements Action {

  readonly type = AuthActionTypes.LoginAction_Clear_Error;

}

export type AuthActions = LoginAction | 
                          LoginSucceedAction | 
                          LoginFailedAction | 
                          LogoutAction | 
                          LoginClearErrorAction;
