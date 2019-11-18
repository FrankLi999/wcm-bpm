import { FuseConfig } from 'bpw-components';
import { ApiConfig } from 'bpw-rest-client';
/**
 * Default Fuse Configuration
 *
 * You can edit these options to change the default options. All these options also can be
 * changed per component basis. See `app/authentication/login/login.component.ts`
 * constructor method to learn more about changing these options per component basis.
 */
export const APP_OAUTH2_REDIRECT_URI = 'http://localhost:3000/oauth2/redirect';
// export const API_BASE_URL = 'http://192.168.0.168:8080';
export const APP_API_BASE_URL = 'http://localhost:8080';
export const appApiConfig: ApiConfig = {
    apiBaseUrl: APP_API_BASE_URL,
    accessToken: 'accessToken',
    oauth2RedirectUrl: APP_OAUTH2_REDIRECT_URI,
    googleAuthUrl: `${APP_API_BASE_URL}/oauth2/authorize/google?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`,
    facebookAuthUrl: `${APP_API_BASE_URL}/oauth2/authorize/facebook?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`,
    githubAuthUrl: `${APP_API_BASE_URL}/oauth2/authorize/github?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`
}

export const appLayoutConfig: FuseConfig = {
    // Color themes can be defined in src/app/app.theme.scss
    colorTheme      : 'theme-default',
    customScrollbars: true,
    layout          : {
        //style    : 'vertical-layout-1',
        style    : 'horizontal-layout-1',
        width    : 'fullwidth',
        navbar   : {
            primaryBackground  : 'fuse-navy-700',
            secondaryBackground: 'fuse-navy-900',
            folded             : false,
            hidden             : true,
            position           : 'left',
            variant            : 'vertical-style-1'
        },
        toolbar  : {
            customBackgroundColor: false,
            background           : 'fuse-white-500',
            hidden               : true,
            position             : 'below-static'
        },
        footer   : {
            customBackgroundColor: true,
            background           : 'fuse-navy-900',
            hidden               : true,
            position             : 'below-fixed'
        },
        sidepanel: {
            hidden  : false,
            position: 'left'
        }
    }
};