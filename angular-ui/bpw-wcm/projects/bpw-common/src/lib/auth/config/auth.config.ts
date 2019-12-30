import { UIConfig } from '../../common/types/ui-config';
/**
 * Default WCM System Configuration
 *
 * You can edit these options to change the default options. All these options also can be
 * changed per component basis. See `app/authentication/login/login.component.ts`
 * constructor method to learn more about changing these options per component basis.
 */
export const authLayoutConfig: UIConfig = {
    // Color themes can be defined in src/app/app.theme.scss
    colorTheme      : 'theme-default',
    customScrollbars: true,
    layout          : {
        //style    : 'vertical-layout-1',
        style    : 'horizontal-layout-1',
        width    : 'fullwidth',
        navbar   : {
            primaryBackground  : 'wcm-navy-700',
            secondaryBackground: 'wcm-navy-900',
            folded             : false,
            hidden             : true,
            position           : 'top',
            variant            : 'vertical-style-1'
        },
        toolbar  : {
            customBackgroundColor: false,
            background           : 'wcm-white-500',
            hidden               : true,
            position             : 'above'
        },
        footer   : {
            customBackgroundColor: true,
            background           : 'wcm-navy-900',
            hidden               : true,
            position             : 'below-static'
        },
        sidepanel: {
            hidden  : true,
            position: 'left'
        }
    }
};