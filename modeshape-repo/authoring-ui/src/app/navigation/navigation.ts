import { FuseNavigation } from '@fuse/types';

export const navigation: FuseNavigation[] = [
    {
        id       : 'applications',
        title    : 'Applications',
        translate: 'NAV.APPLICATIONS',
        type     : 'group',
        children : [
            {
                id       : 'resource-library',
                title    : 'Resource Library',
                translate: 'NAV.RESOURCE_LIBRARY.TITLE',
                type     : 'item',
                icon     : 'archive',
                url      : '/wcm-authoring/resource-library'
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
                id       : 'page-layout',
                title    : 'Page Layout',
                translate: 'NAV.PAGE_LAYOUT.TITLE',
                type     : 'item',
                icon     : 'pageview',
                url      : '/wcm-authoring/page-layout'
            },
            {
                id       : 'resource-type',
                title    : 'Resource Type',
                translate: 'NAV.RESOURCE_TYPE.TITLE',
                type     : 'item',
                icon     : 'settings',
                url      : '/wcm-authoring/resource-type'
            },
            {
                id       : 'render-template',
                title    : 'Render Template',
                translate: 'NAV.RENDER_TEMPLATE.TITLE',
                type     : 'item',
                icon     : 'view_array',
                url      : '/wcm-authoring/render-template'
            },
            {
                id       : 'query-builder',
                title    : 'Query Builder',
                translate: 'NAV.QUERY_BUILDER.TITLE',
                type     : 'item',
                icon     : 'help_outline',
                url      : '/wcm-authoring/query-builder'
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
                url      : '/wcm-authoring/workflow'
            },
            {
                id       : 'validation-rule',
                title    : 'Validation Rule',
                translate: 'NAV.VALIDATION_RULE.TITLE',
                type     : 'item',
                icon     : 'help_outline',
                url      : '/wcm-authoring/validation-rule'
            }
        ]
    }
];
