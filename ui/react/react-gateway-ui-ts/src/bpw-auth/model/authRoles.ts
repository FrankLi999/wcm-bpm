/**
 * Authorization Roles
 */
const authRoles = {
  admin: ['admin'],
  staff: ['admin', 'staff'],
  user: ['user'],
  onlyGuest: [],
};

export default authRoles;
