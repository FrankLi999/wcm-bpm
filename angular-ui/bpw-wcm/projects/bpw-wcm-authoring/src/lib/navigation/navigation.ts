import { Navigation } from 'bpw-common';

export const navigation: Navigation[] = [
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
                url      : '/wcm-authoring/site-explorer/navigator'
            },
            {
                id       : 'content-area-layout',
                title    : 'Content Area Layout',
                translate: 'NAV.CONTENT_AREA_LAYOUT.TITLE',
                type     : 'item',
                icon     : 'pageview',
                url      : '/wcm-authoring/content-area-layout/list'
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
                url      : '/wcm-authoring/render-template/list'
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
                url      : '/wcm-authoring/category/item'
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
