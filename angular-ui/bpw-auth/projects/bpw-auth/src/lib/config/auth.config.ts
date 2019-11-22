import { ApiConfig } from 'bpw-rest-client';
import { FuseConfig } from 'bpw-components';
/**
 * Default Fuse Configuration
 *
 * You can edit these options to change the default options. All these options also can be
 * changed per component basis. See `app/authentication/login/login.component.ts`
 * constructor method to learn more about changing these options per component basis.
 */
export const authLayoutConfig: FuseConfig = {
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
            hidden  : true,
            position: 'left'
        }
    }
};