import { createSlice } from '@reduxjs/toolkit';
import history from 'bpw-common/history';
import jwtService from '../services/jwtService';
import { setUserData } from './userSlice';

export const submitLogin = ({ email, password, loadCustomeData }) => async (dispatch) => {
  return jwtService.signInWithEmailAndPassword(email, password).subscribe(
    (user) => {
      loadCustomeData && loadCustomeData(dispatch, user);
      dispatch(setUserData(user));
      const dispatched = dispatch(loginSuccess());
      history.push({ pathname: history.location.state.redirectUrl });

      return dispatched;
    },
    (error) => {
      return dispatch(loginError(error));
    }
  );
};

const initialState = {
  success: false,
  error: {
    username: null,
    password: null,
  },
};

const loginSlice = createSlice({
  name: 'auth/login',
  initialState,
  reducers: {
    loginSuccess: (state, action) => {
      state.success = true;
    },
    loginError: (state, action) => {
      state.success = false;
      state.error = action.payload;
    },
  },
  extraReducers: {},
});

export const { loginSuccess, loginError } = loginSlice.actions;

export default loginSlice.reducer;
