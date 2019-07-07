import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { FuseSharedModule } from '@fuse/shared.module';

const routes = [
    {
        path        : 'resource-library',
        loadChildren: './resource-library/resource-library.module#ResourceLibraryModule'
    },
    {
        path        : 'site-explorer',
        loadChildren: './site-explorer/site-explorer.module#SiteExplorerModule'
    },
    {
        path        : 'page-layout',
        loadChildren: './page-layout/page-layout.module#PageLayoutModule'
    },
    {
        path        : 'resource-type',
        loadChildren: './resource-type/resource-type.module#ResourceTypeModule'
    },
    {
        path        : 'render-template',
        loadChildren: './render-template/render-template.module#RenderTemplateModule'
    },
    {
        path        : 'query-builder',
        loadChildren: './query-builder/query-builder.module#QueryBuilderModule'
    },
    {
        path        : 'category',
        loadChildren: './category/category.module#CategoryModule'
    },
    {
        path        : 'workflow',
        loadChildren: './workflow/workflow.module#WorkflowModule'
    },
    {
        path        : 'validation-rule',
        loadChildren: './validation-rule/validation-rule.module#ValidationRuleModule'
    }
    // },
    // {
    //     path     : '**',
    //     redirectTo: 'site-explorer'
    // }
];

@NgModule({
    declarations: [
    ],
    imports     : [
        RouterModule.forChild(routes),
        TranslateModule,
        FuseSharedModule
    ],
    exports     : [
    ]
})

export class WcmAuthoringModule
{
}
