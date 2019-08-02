import { NgModule } from '@angular/core';
// import { RouterModule } from '@angular/router';
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
import { FuseSharedModule } from '@fuse/shared.module';
import { FuseSidebarModule } from '@fuse/components';
import { TranslateModule } from '@ngx-translate/core';
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
import { ModeshapeService } from './service/modeshape.service';
import { WcmService } from './service/wcm.service';
import { UploadZipfileDialogComponent } from './dialog/upload-zipfile-dialog/upload-zipfile-dialog.component';
import { FileUploadComponent } from './dialog/file-upload/file-upload.component';
import { NewFolderDialogComponent } from './dialog/new-folder-dialog/new-folder-dialog.component';
import { NewThemeDialogComponent } from './dialog/new-theme-dialog/new-theme-dialog.component';
@NgModule({
    declarations: [
    UploadZipfileDialogComponent,
    FileUploadComponent,
    NewFolderDialogComponent,
    NewThemeDialogComponent],
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

        TranslateModule,

        FuseSharedModule,
        FuseSidebarModule
    ],
    exports     : [
    ],
    providers   : [
        ModeshapeService,
        WcmService
    ],
    entryComponents: [
        UploadZipfileDialogComponent,
        NewFolderDialogComponent,
        NewThemeDialogComponent
    ]
})

export class WcmAuthoringModule
{
}
