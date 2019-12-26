// import { createSelector } from '@ngrx/store';
// export const getAuthContextState = state => state.authContextState;

// export const getUserProfile = createSelector(
//     getAuthContextState,
//     authContextState => authContextState.userProfile
// )

// export const isLoggedIn = createSelector(
//   getAuthContextState,
//   authContextState => authContextState.loggedIn
// )

// export const isLoggedOut = createSelector(
//   getAuthContextState,
//   authContextState => !authContextState.loggedIn
// )

// export const getLoginError = createSelector(
//   getAuthContextState,
//   authContextState => authContextState.loginError
// )

// export const getAccessToken = createSelector(
//   getUserProfile,
//   userProfile => userProfile.accessToken
// );

// export const getTokenType = createSelector(
//   getUserProfile,
//   userProfile => userProfile.tokenType
// );
import { createSelector } from '@ngrx/store';
import { getAuthState } from '../reducers/auth.reducers';

export const getAuthContextState = createSelector(
    getAuthState,
    authState => {
      return authState.authContextState;
    }
  );


export const isLoggedIn = createSelector(
  getAuthContextState,
  authContext => {
    return authContext.loggedIn;
  }
);


export const isLoggedOut = createSelector(
  getAuthContextState,
  authContext => !authContext.loggedIn
);

export const getUserProfile = createSelector(
  getAuthContextState,
  authContext => authContext.userProfile
);


export const getLoginError = createSelector(
  getAuthContextState,
  authContext => {
    return authContext.loginError;
  }
);

export const getAccessToken = createSelector(
  getUserProfile,
  userProfile => userProfile.accessToken
);

export const getTokenType = createSelector(
  getUserProfile,
  userProfile => userProfile.tokenType
);