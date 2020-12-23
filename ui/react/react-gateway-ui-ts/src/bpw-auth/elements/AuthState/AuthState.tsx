import React, { useState, useEffect } from 'react';
import { of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { connect } from 'react-redux';
import { bindActionCreators } from '@reduxjs/toolkit';

import AppSplashScreen from 'bpw-common/elements/AppSplashScreen';
import { hideMessage, showMessage } from 'bpw-common/store/messageSlice';
import jwtService from '../../services/jwtService';
import { setUserData, logoutUser } from '../../store/userSlice';

function AuthState(props) {
  const [waitAuthCheck, setWaitAuthCheck] = useState(true);
  useEffect(() => {
    jwtCheck();
  }, []);

  const jwtCheck = () => {
    jwtService.setInterceptors();
    const result = jwtService.checkAccessToken();
    if (result.event === 'NoAccessToken') {
      // do nothing
      setWaitAuthCheck(false);
    } else if (result.event === 'AutoLogout') {
      if (result.message) {
        props.showMessage({ message: result.message });
      }
      props.logout();
      setWaitAuthCheck(false);
    } else if (result.event === 'AutoLogin') {
      props.showMessage({ message: 'Logging in with JWT' });
      jwtService
        .signInWithToken()
        .pipe(
          tap((user) => {
            props.setUserData({
              ...user,
              accessToken: sessionStorage.getItem('bpw_accessToken'),
              tokenType: sessionStorage.getItem('bpw_tokenType'),
              expireIn: sessionStorage.getItem('bpw_expireIn'),
            });
            props.showMessage({ message: 'Logged in with JWT' });
          }),
          catchError((error) => {
            props.showMessage({ message: error });
            return of(error);
          })
        )
        .subscribe(() => {
          setWaitAuthCheck(false);
        });
    }
  };

  return waitAuthCheck ? <AppSplashScreen /> : <>{props.children}</>;
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(
    {
      logout: logoutUser,
      setUserData,
      showMessage,
      hideMessage,
    },
    dispatch
  );
}

export default connect(null, mapDispatchToProps)(AuthState);
