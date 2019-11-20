import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';

import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';

import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatMomentDateModule } from '@angular/material-moment-adapter';
import { MatProgressBarModule} from "@angular/material";
import { MatRippleModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';

import { TranslateModule } from '@ngx-translate/core';
import { AceEditorModule } from 'ng2-ace-editor';

import {
  // FuseConfigModule,
  FuseSharedModule,
  FuseProgressBarModule,
  FuseSidebarModule,
  FuseThemeOptionsModule
} from 'bpw-components';

// import { RestClientConfigModule } from 'bpw-rest-client';
import { LayoutModule } from 'bpw-layout';

import {
  JsonSchemaFormModule, 
  // MaterialDesignFrameworkModule
} from 'bpw-form';

import { AuthGuard } from 'bpw-auth-store';
// import { wcmAuthoringLayoutConfig, wcmAuthoringApiConfig } from '../wcm-authoring-config';
import { WcmAppStoreModule } from 'bpw-wcm-service';
import { ComponentModule } from './components/component.module';
import { JcrExplorerModule } from './jcr-explorer/jcr-explorer.module';
import { ResourceLibraryModule } from './resource-library/resource-library.module';
import { SiteExplorerModule } from './site-explorer/site-explorer.module';
import { ContentAreaLayoutModule } from './content-area-layout/content-area-layout.module';
import { ResourceTypeModule } from './resource-type/resource-type.module';
import { RenderTemplateModule } from './render-template/render-template.module';
import { QueryBuilderModule } from './query-builder/query-builder.module';
import { CategoryModule } from './category/category.module';
import { WorkflowModule } from './workflow/workflow.module';
import { ValidationRuleModule } from './validation-rule/validation-rule.module';
import { PreviewModule } from 'bpw-wcm-preview';
import { UploadZipfileDialogComponent } from './dialog/upload-zipfile-dialog/upload-zipfile-dialog.component';
import { FileUploadComponent } from './dialog/file-upload/file-upload.component';
import { NewFolderDialogComponent } from './dialog/new-folder-dialog/new-folder-dialog.component';
import { NewThemeDialogComponent } from './dialog/new-theme-dialog/new-theme-dialog.component';
import { NewSiteareaDialogComponent } from './dialog/new-sitearea-dialog/new-sitearea-dialog.component';
import { NewContentDialogComponent } from './dialog/new-content-dialog/new-content-dialog.component';
import { NewSiteConfigDialogComponent } from './dialog/new-site-config-dialog/new-site-config-dialog.component';

import * as fromGuards from 'bpw-wcm-service';
import { WcmAuthoringComponent } from './entry/wcm-authoring/wcm-authoring.component';
import { JcrExplorerComponent } from './jcr-explorer/jcr-explorer/jcr-explorer.component';
import { CategoryTreeComponent } from './category/category-tree/category-tree.component';
import { CategoryComponent } from './category/category/category.component';
import { ContentAreaLayoutTreeComponent } from './content-area-layout/content-area-layout-tree/content-area-layout-tree.component';
import { ContentAreaLayoutsComponent } from './content-area-layout/content-area-layouts/content-area-layouts.component';
import { ContentAreaDesignerComponent } from './content-area-layout/content-area-designer/content-area-designer.component';
import { ContentAreaPreviewComponent } from 'bpw-wcm-preview';
import { QueryTreeComponent } from './query-builder/query-tree/query-tree.component';
import { QueryEditorComponent } from './query-builder/query-editor/query-editor.component';
import { QueryListComponent } from './query-builder/query-list/query-list.component';
import { RenderTemplateTreeComponent } from './render-template/render-template-tree/render-template-tree.component';
import { RenderTemplateComponent } from './render-template/render-template/render-template.component';
import { RenderTemplatesComponent } from './render-template/render-templates/render-templates.component';
import { ResourceLibraryTreeComponent } from './resource-library/resource-library-tree/resource-library-tree.component';
import { LibraryComponent } from './resource-library/library/library.component';
import { LibrariesComponent } from './resource-library/libraries/libraries.component';
import { ResourceTypeTreeComponent } from './resource-type/resource-type-tree/resource-type-tree.component';
import { ResourceTypeListComponent } from './resource-type/resource-type-list/resource-type-list.component';
import { ResourceTypeEditorComponent } from './resource-type/resource-type-editor/resource-type-editor.component';
import { SiteTreeComponent } from './site-explorer/site-tree/site-tree.component';
import { SiteAreaComponent } from './site-explorer/site-area/site-area.component';
import { ContentItemComponent } from './site-explorer/content-item/content-item.component';
import { SiteExplorerComponent } from './site-explorer/site-explorer/site-explorer.component';
import { ValidationRuleTreeComponent } from './validation-rule/validation-rule-tree/validation-rule-tree.component';
import { ValidationRulesComponent } from './validation-rule/validation-rules/validation-rules.component';
import { ValidationRuleComponent } from './validation-rule/validation-rule/validation-rule.component';
import { WorkflowTreeComponent } from './workflow/workflow-tree/workflow-tree.component'; 
import { WorkflowsComponent } from './workflow/workflows/workflows.component'; 
import { WorkflowComponent } from './workflow/workflow/workflow.component'; 

const routes: Routes = [{
  path: '',
  component: WcmAuthoringComponent,
  canActivate: [AuthGuard, fromGuards.ResolveGuard],
  children: [
    {
      path: 'jcr-explorer',
      component: JcrExplorerComponent    
    },
    {
      path: 'category',
      component: CategoryTreeComponent,
      children: [
        {
          path       : 'item',
          component  :  CategoryComponent
        }
      ]
    },
    {
      path: 'content-area-layout',
      component: ContentAreaLayoutTreeComponent,
      children: [
        {
            path       : 'edit',
            component  : ContentAreaDesignerComponent
        },
        {
            path       : 'new',
            component  : ContentAreaDesignerComponent
        },
        {
            path       : 'list',
            component  : ContentAreaLayoutsComponent
        }
      ]
    },
    {
      path     : 'preview',
      component: ContentAreaPreviewComponent   
    },
    {
      path: 'query-builder',
      component: QueryTreeComponent,
      children: [
        {
            path       : 'edit',
            component  : QueryEditorComponent
        },
        {
            path       : 'list',
            component  : QueryListComponent
        }
      ]
    },
    {
      path: 'render-template',
      component: RenderTemplateTreeComponent,
      children: [
        {
            path       : 'edit',
            component  : RenderTemplateComponent
        },
        {
          path       : 'new',
          component  : RenderTemplateComponent
        },
        {
            path       : 'list',
            component  : RenderTemplatesComponent
        }
      ]
    },
    {
      path: 'resource-library',
      component: ResourceLibraryTreeComponent,
      children: [{
          path       : 'edit',
          component  : LibraryComponent
        },
        {
            path       : 'list',
            component  : LibrariesComponent
        }
      ]
    },
    {
      path: 'resource-type',
      component: ResourceTypeTreeComponent,
      children: [
        {
            path       : 'edit',
            component  : ResourceTypeEditorComponent
        },
        {
          path       : 'new',
          component  : ResourceTypeEditorComponent
        },
        {
            path       : 'list',
            component  : ResourceTypeListComponent
        }
      ]
    },
    {
      path: 'site-explorer', 
      component: SiteTreeComponent, 
      children: [
        {
            path       : 'navigator',
            component  : SiteExplorerComponent
        },
        {
          path       : 'new-content',
          component  : ContentItemComponent
        },
        {
          path       : 'edit-content',
          component  : ContentItemComponent
        },
        {
          path       : 'new-sa',
          component  : SiteAreaComponent
        },
        {
          path       : 'edit-sa',
          component  : SiteAreaComponent
        }
      ]
    },
    {
      path: 'validation-rule',
      component: ValidationRuleTreeComponent,
      children: [
        {
            path       : 'edit',
            component  : ValidationRuleComponent
        },
        {
            path       : 'list',
            component  : ValidationRulesComponent
        }
      ]
    },
    {
      path: 'workflow',
      component: WorkflowTreeComponent,
      children: [
        {
            path       : 'edit',
            component  : WorkflowComponent
        },
        {
            path       : 'list',
            component  : WorkflowsComponent
        }
      ]
    }
  ]
}];
@NgModule({
    declarations: [
      UploadZipfileDialogComponent,
      FileUploadComponent,
      NewFolderDialogComponent,
      NewThemeDialogComponent,
      NewSiteareaDialogComponent,
      NewContentDialogComponent,
      NewSiteConfigDialogComponent,
      WcmAuthoringComponent
    ],
    imports : [
        RouterModule.forChild(routes),
        CommonModule,
        FormsModule, 
        ReactiveFormsModule,
        FlexLayoutModule,
        MatButtonModule,
        MatCheckboxModule,        
        MatDialogModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        
        MatMenuModule,
        MatMomentDateModule,
        MatProgressBarModule,
        MatRippleModule,
        MatSelectModule,
        MatToolbarModule,

        JsonSchemaFormModule,
        TranslateModule,

        FuseSharedModule,
        FuseSidebarModule,
        AceEditorModule,
       
        FuseProgressBarModule,
        FuseThemeOptionsModule,

        LayoutModule,
 
        WcmAppStoreModule,
        ComponentModule,
        JcrExplorerModule,
        ResourceLibraryModule,
        SiteExplorerModule,
        ContentAreaLayoutModule,
        ResourceTypeModule,
        RenderTemplateModule,
        QueryBuilderModule,
        CategoryModule,
        WorkflowModule,
        ValidationRuleModule,
        PreviewModule,
    ],
    providers   : [
      fromGuards.ResolveGuard
    ],
    exports: [
      WcmAuthoringComponent
    ],
    entryComponents: [
        UploadZipfileDialogComponent,
        NewFolderDialogComponent,
        NewThemeDialogComponent,
        NewSiteareaDialogComponent,
        NewContentDialogComponent,
        NewSiteConfigDialogComponent
    ]
})
export class WcmAuthoringModule {
}
