import { authRoles } from '../../model';
import LoginPage from './LoginPage';

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
      component: LoginPage,
    },
  ],
};

export default LoginConfig;
