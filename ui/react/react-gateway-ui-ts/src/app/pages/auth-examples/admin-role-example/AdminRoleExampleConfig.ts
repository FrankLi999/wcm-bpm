import { authRoles } from 'bpw-auth/model';
import AdminRoleExample from './AdminRoleExample';

const AdminRoleExampleConfig = {
  settings: {
    layout: {
      // config: {},
    },
  },
  routes: [
    {
      path: '/pages/authorization/admin-role-example',
      component: AdminRoleExample,
      auth: authRoles.admin, // ['admin']
    },
  ],
};

export default AdminRoleExampleConfig;
