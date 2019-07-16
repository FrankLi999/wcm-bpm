import { NgModule } from '@angular/core';
// import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { FuseSharedModule } from '@fuse/shared.module';
import { JcrExplorerModule } from './jcr-explorer/jcr-explorer.module';
import { ResourceLibraryModule } from './resource-library/resource-library.module';
import { SiteExplorerModule } from './site-explorer/site-explorer.module';
import { PageLayoutModule } from './page-layout/page-layout.module';
import { ResourceTypeModule } from './resource-type/resource-type.module';
import { RenderTemplateModule } from './render-template/render-template.module';
import { QueryBuilderModule } from './query-builder/query-builder.module';
import { CategoryModule } from './category/category.module';
import { WorkflowModule } from './workflow/workflow.module';
import { ValidationRuleModule } from './validation-rule/validation-rule.module';

@NgModule({
    declarations: [
    ],
    imports : [
        // RouterModule.forChild(routes),
        JcrExplorerModule,
        ResourceLibraryModule,
        SiteExplorerModule,
        PageLayoutModule,
        ResourceTypeModule,
        RenderTemplateModule,
        QueryBuilderModule,
        CategoryModule,
        WorkflowModule,
        ValidationRuleModule,
        TranslateModule,
        FuseSharedModule
    ],
    exports     : [
    ]
})

export class WcmAuthoringModule
{
}
