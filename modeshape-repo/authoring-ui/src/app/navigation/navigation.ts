import { FuseNavigation } from '@fuse/types';

export const navigation: FuseNavigation[] = [
    {
        id      : 'authentication',
        title   : 'Authentication',
        type    : 'group',
        icon    : 'lock',
        children: [
            {
                id       : 'login',
                title    : 'Login',
                translate: 'NAV.LOGIN.TITLE',
                type     : 'item',
                icon     : 'help_outline',
                url      : '/auth/login'
            },
            {
                id       : 'forgot-password',
                title    : 'Forgot Password',
                translate: 'NAV.FORGOT_PASSWORD.TITLE',
                type     : 'item',
                icon     : 'help_outline',
                url      : '/auth/forgot-password'
            },
            {
                id       : 'reset-password',
                title    : 'Reset Password',
                translate: 'NAV.RESET_PASSWORD.TITLE',
                type     : 'item',
                icon     : 'help_outline',
                url      : '/auth/reset-password'
            },
            {
                id       : 'signup',
                title    : 'Signup',
                translate: 'NAV.SIGNUP.TITLE',
                type     : 'item',
                icon     : 'help_outline',
                url      : '/auth/signup'
            },
            {
                id       : 'lock-screen',
                title    : 'Lock Screen',
                translate: 'NAV.LOCK_SCREEN.TITLE',
                type     : 'item',
                icon     : 'help_outline',
                url      : '/auth/lock-screen'
            },
            {
                id       : 'mail-confirm',
                title    : 'Mail Confirm',
                translate: 'NAV.MAIL_CONFIRM.TITLE',
                type     : 'item',
                icon     : 'help_outline',
                url      : '/auth/mail-confirm'
            }
        ]
    },
    {
        id      : 'wcm-authoring',
        title   : 'WCM Authoring',
        type    : 'group',
        icon    : 'lock',
        children: [
            {
                id       : 'jcr-explorer',
                title    : 'JCR Explorer',
                translate: 'NAV.JCR_EXPLORER.TITLE',
                type     : 'item',
                icon     : 'archive',
                url      : '/wcm-authoring/jcr-explorer'
            },
            {
                id       : 'resource-library',
                title    : 'Resource Library',
                translate: 'NAV.RESOURCE_LIBRARY.TITLE',
                type     : 'item',
                icon     : 'archive',
                url      : '/wcm-authoring/resource-library/list'
            },
            {
                id       : 'site-explorer',
                title    : 'Site Explorer',
                translate: 'NAV.SITE_EXPLORER.TITLE',
                type     : 'item',
                icon     : 'archive',
                url      : '/wcm-authoring/site-explorer'
            },
            {
                id       : 'content-area-layout',
                title    : 'Content Area Layout',
                translate: 'NAV.CONTENT_AREA_LAYOUT.TITLE',
                type     : 'item',
                icon     : 'pageview',
                url      : '/wcm-authoring/content-area-layout/edit'
            },
            {
                id       : 'resource-type',
                title    : 'Resource Type',
                translate: 'NAV.RESOURCE_TYPE.TITLE',
                type     : 'item',
                icon     : 'settings',
                url      : '/wcm-authoring/resource-type/list'
            },
            {
                id       : 'render-template',
                title    : 'Render Template',
                translate: 'NAV.RENDER_TEMPLATE.TITLE',
                type     : 'item',
                icon     : 'view_array',
                url      : '/wcm-authoring/render-template/edit'
            },
            {
                id       : 'query-builder',
                title    : 'Query Builder',
                translate: 'NAV.QUERY_BUILDER.TITLE',
                type     : 'item',
                icon     : 'help_outline',
                url      : '/wcm-authoring/query-builder/list'
            }, 
            {
                id       : 'category',
                title    : 'Category',
                translate: 'NAV.CATEGORY.TITLE',
                type     : 'item',
                icon     : 'help_outline',
                url      : '/wcm-authoring/category'
            }, 
            {
                id       : 'workflow',
                title    : 'Workflow',
                translate: 'NAV.WORKFLOW.TITLE',
                type     : 'item',
                icon     : 'help_outline',
                url      : '/wcm-authoring/workflow/list'
            },
            {
                id       : 'validation-rule',
                title    : 'Validation Rule',
                translate: 'NAV.VALIDATION_RULE.TITLE',
                type     : 'item',
                icon     : 'help_outline',
                url      : '/wcm-authoring/validation-rule/list'
            }
        ]
    }

];
