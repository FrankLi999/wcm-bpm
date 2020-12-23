import { Navigation } from 'bpw-common';

export const navigation: Navigation[] = [
    {
        id       : 'applications',
        title    : 'Applications',
        translate: 'NAV.APPLICATIONS',
        type     : 'group',
        children : [
            {
                id       : 'sample',
                title    : 'Sample',
                translate: 'NAV.SAMPLE.TITLE',
                type     : 'item',
                icon     : 'email',
                url      : '/sample',
                badge    : {
                    title    : '25',
                    translate: 'NAV.SAMPLE.BADGE',
                    bg       : '#F44336',
                    fg       : '#FFFFFF'
                }
            }
        ]
    },
    {
        id       : 'forms',
        title    : 'Forms',
        translate: 'NAV.FORMS',
        type     : 'group',
        children : [
            {
                id       : 'jsonForm',
                title    : 'Json Form',
                translate: 'NAV.JSON_FORM',
                type     : 'item',
                icon     : 'email',
                url      : '/forms/json-form',
                badge    : {
                    title    : '25',
                    translate: 'NAV.JSON_FORM.BADGE',
                    bg       : '#F44336',
                    fg       : '#FFFFFF'
                }
            }
        ]
    }
];
