import { FuseConfig } from 'bpw-components';
import { ApiConfig } from 'bpw-rest-client';
/**
 * Default Fuse Configuration
 *
 * You can edit these options to change the default options. All these options also can be
 * changed per component basis. See `app/authentication/login/login.component.ts`
 * constructor method to learn more about changing these options per component basis.
 */
export const OAUTH2_REDIRECT_URI = 'http://localhost:3000/oauth2/redirect';
export const API_BASE_URL = 'http://192.168.0.168:8080';
export const apiConfig: ApiConfig = {
    apiBaseUrl: API_BASE_URL,
    accessToken: 'accessToken',
    oauth2RedirectUrl: OAUTH2_REDIRECT_URI,
    googleAuthUrl: `${API_BASE_URL}/oauth2/authorize/google?redirect_uri=${OAUTH2_REDIRECT_URI}`,
    facebookAuthUrl: `${API_BASE_URL}/oauth2/authorize/facebook?redirect_uri=${OAUTH2_REDIRECT_URI}`,
    githubAuthUrl: `${API_BASE_URL}/oauth2/authorize/github?redirect_uri=${OAUTH2_REDIRECT_URI}`
}

export const fuseConfig: FuseConfig = {
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
            hidden             : false,
            position           : 'left',
            variant            : 'vertical-style-1'
        },
        toolbar  : {
            customBackgroundColor: false,
            background           : 'fuse-white-500',
            hidden               : false,
            position             : 'below-static'
        },
        footer   : {
            customBackgroundColor: true,
            background           : 'fuse-navy-900',
            hidden               : false,
            position             : 'below-fixed'
        },
        sidepanel: {
            hidden  : false,
            position: 'left'
        }
    }
};
