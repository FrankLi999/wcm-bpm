import { authRoles } from 'bpw-auth/model';
import UserRoleExample from './UserRoleExample';

const UserRoleExampleConfig = {
  settings: {
    layout: {
      // config: {},
    },
  },
  routes: [
    {
      path: '/pages/authorization/user-role-example',
      component: UserRoleExample,
      auth: authRoles.user, // ['user']
    },
  ],
};

export default UserRoleExampleConfig;
