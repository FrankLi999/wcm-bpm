import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatRippleModule } from '@angular/material/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import {
  MatProgressBarModule
} from "@angular/material";

import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';
import { FuseSharedModule, FuseSidebarModule } from 'bpw-components';
import { TranslateModule } from '@ngx-translate/core';
import { AceEditorModule } from 'ng2-ace-editor';
import {
  JsonSchemaFormModule, 
  MaterialDesignFrameworkModule
} from 'bpw-form';

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
import { PreviewModule } from './preview/preview.module';
import { ModeshapeService } from './service/modeshape.service';
import { WcmService } from './service/wcm.service';
// import { RendererService } from './preview/renderer.service';
import { UploadZipfileDialogComponent } from './dialog/upload-zipfile-dialog/upload-zipfile-dialog.component';
import { FileUploadComponent } from './dialog/file-upload/file-upload.component';
import { NewFolderDialogComponent } from './dialog/new-folder-dialog/new-folder-dialog.component';
import { NewThemeDialogComponent } from './dialog/new-theme-dialog/new-theme-dialog.component';
import { NewSiteareaDialogComponent } from './dialog/new-sitearea-dialog/new-sitearea-dialog.component';
import { NewContentDialogComponent } from './dialog/new-content-dialog/new-content-dialog.component';
import { NewSiteConfigDialogComponent } from './dialog/new-site-config-dialog/new-site-config-dialog.component';
import { WcmAppStoreModule } from './store/store.module';
import { AuthenticationModule } from 'bpw-auth';
import { OAuth2Module } from 'bpw-auth';
import { AuthHttpInterceptor } from 'bpw-store';
import * as fromGuards from './store/guards';
const routes: Routes = [
  {
      path      : '**',
      redirectTo: 'site-explorer/navigator'
      // ,canActivate: [fromGuards.ResolveGuard]
  }
];
@NgModule({
    declarations: [
      UploadZipfileDialogComponent,
      FileUploadComponent,
      NewFolderDialogComponent,
      NewThemeDialogComponent,
      NewSiteareaDialogComponent,
      NewContentDialogComponent,
      NewSiteConfigDialogComponent
    ],
    imports : [
        RouterModule.forChild(routes),
        CommonModule,
        FormsModule, 
        ReactiveFormsModule,
        HttpClientModule,
        FlexLayoutModule,
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
        MatButtonModule,
        MatCheckboxModule,
        MatDialogModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatMenuModule,
        MatProgressBarModule,
        MatRippleModule,
        MatSelectModule,
        MatToolbarModule,

        MaterialDesignFrameworkModule,
        JsonSchemaFormModule,
        TranslateModule,

        FuseSharedModule,
        FuseSidebarModule,
        WcmAppStoreModule,
        AceEditorModule,
        AuthenticationModule,
        OAuth2Module
    ],
    exports     : [
    ],
    providers   : [
        fromGuards.ResolveGuard,
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AuthHttpInterceptor,
          multi: true
      }
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
