import { authRoles } from 'bpw-auth/model';
import GuestRoleExample from './GuestRoleExample';

const GuestRoleExampleConfig = {
  settings: {
    layout: {
      // config: {},
    },
  },

  routes: [
    {
      path: '/pages/authorization/guest-role-example',
      component: GuestRoleExample,
      auth: authRoles.onlyGuest, // ['guest']
    },
  ],
};

export default GuestRoleExampleConfig;
