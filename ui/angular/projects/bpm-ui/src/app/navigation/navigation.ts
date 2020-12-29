import { Navigation } from 'bpw-common';

export const navigation: Navigation[] = [
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
    }
];
