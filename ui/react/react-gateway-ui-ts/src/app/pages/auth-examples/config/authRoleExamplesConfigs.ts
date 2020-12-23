import i18next from 'i18next';
import AdminRoleExampleConfig from '../admin-role-example/AdminRoleExampleConfig';
import GuestRoleExampleConfig from '../only-guest-role-example/GuestRoleExampleConfig';
import UserRoleExampleConfig from '../user-role-example/UserRoleExampleConfig';
import nav_en from './navigation-i18n/en';
import nav_fr from './navigation-i18n/fr';

i18next.addResourceBundle('en', 'navigation', nav_en, true);
i18next.addResourceBundle('fr', 'navigation', nav_fr, true);
const authRoleExamplesConfigs = [AdminRoleExampleConfig, UserRoleExampleConfig, GuestRoleExampleConfig];

export default authRoleExamplesConfigs;
