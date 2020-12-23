import i18next from 'i18next';
import { authRoles } from 'bpw-auth/model';
import GatewayPage from '../wcm/Gateway';
import en from './i18n/en';
import fr from './i18n/fr';
import nav_en from './navigation-i18n/en';
import nav_fr from './navigation-i18n/fr';

i18next.addResourceBundle('en', 'apigateway', en);
i18next.addResourceBundle('fr', 'apigateway', fr);

i18next.addResourceBundle('en', 'navigation', nav_en, true);
i18next.addResourceBundle('fr', 'navigation', nav_fr, true);
const GatewayConfig = {
  settings: {
    layout: {
      // config: {},
    },
  },
  routes: [
    {
      path: '/apigateway/**',
      component: GatewayPage,
      auth: authRoles.admin, // ['admin']
    },
    // {
    //   path: '/apigateway/home',
    //   component: GatewayPage,
    // },
    // {
    //   path: '/apigateway/handlers/firewall',
    //   component: GatewayPage,
    // },
    // {
    //   path: '/apigateway/handlers/sign',
    //   component: GatewayPage,
    // },
    // {
    //   path: '/apigateway/handlers/monitor',
    //   component: GatewayPage,
    // },
    // {
    //   path: '/apigateway/handlers/rewrite',
    //   component: GatewayPage,
    // },
    // {
    //   path: '/apigateway/handlers/rate_limiter',
    //   component: GatewayPage,
    // },
    // {
    //   path: '/apigateway/handlers/divide',
    //   component: GatewayPage,
    // },
    // {
    //   path: '/apigateway/handlers/springCloud',
    //   component: GatewayPage,
    // },
    // {
    //   path: '/apigateway/handlers/hystrix',
    //   component: GatewayPage,
    // },
    // {
    //   path: '/apigateway/system/handlers',
    //   component: GatewayPage,
    // },
    // {
    //   path: '/apigateway/system/metadata',
    //   component: GatewayPage,
    // },
    // {
    //   path: '/apigateway/system/certificate',
    //   component: GatewayPage,
    // },
    // {
    //   path: '/apigateway/system/account',
    //   component: GatewayPage,
    // },
  ],
};

export default GatewayConfig;
