import { UserProfile } from '../model/user-profile.model';
import { AuthActions, AuthActionTypes} from '../actions/auth.actions';
import { ActionReducerMap, createFeatureSelector } from '@ngrx/store';

export interface AuthContextState {
  loggedIn: boolean,
  loginError?: string,
  userProfile: UserProfile
}

export const initialAuthContextState: AuthContextState = {
  loggedIn: false,
  userProfile: {
    id: 'anunymous',
    email: '',
    accessToken: '',
    roles: [],
    tokenType: 'Bearer'
  },
  loginError: undefined
};

export function authReducer(state = initialAuthContextState,
                            action: AuthActions): AuthContextState {
  switch (action.type) {

    case AuthActionTypes.LoginAction_Succeed:
      return {
        loggedIn: true,
        loginError: undefined,
        userProfile: {
          ... action.payload
        }
      }
    case AuthActionTypes.LoginAction_Failed:
      return {
        ... initialAuthContextState,
        loginError: action.payload
        
      };  

    case AuthActionTypes.LogoutAction:
        return {
          ... initialAuthContextState
        };
    case AuthActionTypes.LoginAction_Clear_Error:
      return {
        ...state,
        loginError: undefined
      };
    default:
      return state;
  }
}

export const authFeatureKey = 'authState';

export interface AuthState {
    authContextState: AuthContextState;
}

export const authReducers: ActionReducerMap<AuthState> = {
    authContextState  : authReducer
};

export const getAuthState = createFeatureSelector<AuthState>(
  authFeatureKey
);