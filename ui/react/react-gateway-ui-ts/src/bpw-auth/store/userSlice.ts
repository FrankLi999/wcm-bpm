import { createSlice } from '@reduxjs/toolkit';
import _ from 'bpw-common/lodash';
import { setInitialSettings, setDefaultSettings } from 'bpw-common/store/settingsSlice';
import { appendNavigationItem, prependNavigationItem } from 'bpw-common/store/navigationSlice';
import { showMessage } from 'bpw-common/store/messageSlice';
import jwtService from '../services/jwtService';
import history from 'bpw-common/history';
export const setUserData = (user) => async (dispatch, getState) => {
  /*
        You can redirect the logged-in user to a specific route depending on his role
         */

  const redirectUrl =
    history.location.state && history.location.state.redirectUrl ? history.location.state.redirectUrl : user.redirectUrl ? user.redirectUrl : '/';
  history.location.state = {
    redirectUrl: redirectUrl,
  };
  /*
    Set User Settings
     */

  const settings = user.settings;
  if (settings) {
    dispatch(
      setDefaultSettings({
        animations: settings.siteConfig.animations,
        direction: settings.siteConfig.direction,
        customScrollbars: settings.siteConfig.customScrollbars,
        theme: {
          ...settings.siteConfig.themeColors,
        },
        layout: {
          // style: settings.siteConfig.layout.title,
          title: settings.siteConfig.layout.title,
          // config: {
            mode: settings.siteConfig.layout.mode,
            scroll: settings.siteConfig.layout.scroll,
            footer: {
              ...settings.siteConfig.layout.footer,
            },
            leftSidePanel: {
              ...settings.siteConfig.layout.leftSidePanel,
            },
            navbar: {
              ...settings.siteConfig.layout.navbar,
            },
            rightSidePanel: {
              ...settings.siteConfig.layout.rightSidePanel,
            },
            toolbar: {
              ...settings.siteConfig.layout.toolbar,
            },
          // },
        },
      })
    );

    settings.navigations.forEach((nav) => {
      if ('Home' === nav.title) {
        dispatch(prependNavigationItem(nav));
      } else {
        dispatch(appendNavigationItem(nav));
      }
    });
  }
  user.settings = null;
  delete user.settins;
  sessionStorage.setItem('bpw_accessToken', user.accessToken);
  sessionStorage.setItem('bpw_expireIn', user.expireIn);
  sessionStorage.setItem('bpw_tokenType', user.tokenType);
  return dispatch(setUser(user));
};

export const updateUserSettings = (settings) => async (dispatch, getState) => {
  const oldUser = getState().auth.user;
  const user = _.merge({}, oldUser, { data: { settings } });

  dispatch(updateUserData(user));

  return dispatch(setUserData(user));
};

export const updateUserShortcuts = (shortcuts) => async (dispatch, getState) => {
  const { user } = getState().autu;
  const newUser = {
    ...user,
    data: {
      ...user.data,
      shortcuts,
    },
  };

  dispatch(updateUserData(user));

  return dispatch(setUserData(newUser));
};

export const logoutUser = (customeCleanup) => async (dispatch, getState) => {
	const { user } = getState().auth;

	if (!user.roles || user.roles.length === 0) {
		// is guest
		return null;
  }
  sessionStorage.removeItem('bpw_accessToken');
  sessionStorage.removeItem('bpw_expireIn');
  sessionStorage.removeItem('bpw_tokenType');

  jwtService.logout();

  dispatch(setInitialSettings());
  customeCleanup && customeCleanup(dispatch);
  dispatch(setInitialSettings());
  history.push({
		pathname: '/'
  });
  return dispatch(userLoggedOut());
};

export const updateUserData = (user) => async (dispatch, getState) => {
  if (!user.role || user.role.length === 0) {
    // is guest
    return;
  }
  switch (user.from) {
    default: {
      jwtService
        .updateUserData(user)
        .then(() => {
          dispatch(showMessage({ message: 'User data saved with api' }));
        })
        .catch((error) => {
          dispatch(showMessage({ message: error.message }));
        });
      break;
    }
  }
};

const initialState = {
  role: [], // guest
  data: {
    displayName: 'John Doe',
    photoURL: '/assets/images/avatars/profile.jpg',
    email: 'johndoe@example.com',
    shortcuts: ['calendar', 'mail', 'contacts', 'todo'],
  },
};

const userSlice = createSlice({
  name: 'auth/user',
  initialState,
  reducers: {
    setUser: (state, action) => action.payload,
    userLoggedOut: (state, action) => initialState,
  },
  extraReducers: {},
});

export const { setUser, userLoggedOut } = userSlice.actions;

export default userSlice.reducer;
