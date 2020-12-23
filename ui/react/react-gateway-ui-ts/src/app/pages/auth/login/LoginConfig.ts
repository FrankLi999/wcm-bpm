import { authRoles } from 'bpw-auth/model';
import Login from './Login';

const LoginConfig = {
  settings: {
    layout: {
      // config: {
        navbar: {
          display: false,
        },
        toolbar: {
          display: false,
        },
        footer: {
          display: false,
        },
        leftSidePanel: {
          display: false,
        },
        rightSidePanel: {
          display: false,
        },
      // },
    },
  },
  auth: authRoles.onlyGuest,
  routes: [
    {
      path: '/auth/login',
      component: Login,
    },
  ],
};

export default LoginConfig;
