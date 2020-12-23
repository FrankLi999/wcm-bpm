import React from 'react';
import LoginPage from 'bpw-auth/pages/login/LoginPage';
import { setWcmSystem } from 'bpw-wcm/store/wcmSystemSlice';
import { loadEnum } from '../../../store/system/enumSlice';
const loadCustomeData = (dispatch, user) => {
  if (user.settings) {
    dispatch(setWcmSystem(user.settings));
  }
  return dispatch(loadEnum());
}
function Login(props) {
  return <LoginPage loadCustomeData={loadCustomeData}></LoginPage>;
}

export default Login;
