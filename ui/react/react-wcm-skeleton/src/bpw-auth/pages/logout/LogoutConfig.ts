import { authRoles } from '../../model';
import { logoutUser } from '../../store/userSlice';

const LogoutConfig = {
  auth: authRoles.user,
  routes: [
    {
      path: '/auth/logout',
      component: () => {
        logoutUser();
        return 'Logging out..';
      },
    },
  ],
};

export default LogoutConfig;
