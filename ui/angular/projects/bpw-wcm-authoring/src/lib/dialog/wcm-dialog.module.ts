import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FlexLayoutModule } from "@angular/flex-layout";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";

import { MatInputModule } from "@angular/material/input";
import { MatMenuModule } from "@angular/material/menu";
import { MatMomentDateModule } from "@angular/material-moment-adapter";
import { MatProgressBarModule } from "@angular/material/progress-bar";
import { MatRippleModule } from "@angular/material/core";
import { MatSelectModule } from "@angular/material/select";
import { MatToolbarModule } from "@angular/material/toolbar";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { JsonSchemaFormModule } from "@bpw/ajsf-core";
import { MaterialDesignFrameworkModule } from "@bpw/ajsf-material";
import { UploadZipfileDialogComponent } from "./upload-zipfile-dialog/upload-zipfile-dialog.component";
import { FileUploadComponent } from "./file-upload/file-upload.component";
import { NewFolderDialogComponent } from "./new-folder-dialog/new-folder-dialog.component";
import { NewThemeDialogComponent } from "./new-theme-dialog/new-theme-dialog.component";
import { NewSiteareaDialogComponent } from "./new-sitearea-dialog/new-sitearea-dialog.component";
import { NewContentDialogComponent } from "./new-content-dialog/new-content-dialog.component";
import { NewSiteConfigDialogComponent } from "./new-site-config-dialog/new-site-config-dialog.component";
import { NewLibraryDialogComponent } from "./new-library-dialog/new-library-dialog.component";
import { NewCategoryDialogComponent } from "./new-category-dialog/new-category-dialog.component";

@NgModule({
  declarations: [
    UploadZipfileDialogComponent,
    FileUploadComponent,
    NewFolderDialogComponent,
    NewThemeDialogComponent,
    NewSiteareaDialogComponent,
    NewContentDialogComponent,
    NewSiteConfigDialogComponent,
    NewLibraryDialogComponent,
    NewCategoryDialogComponent,
  ],
  imports: [
    CommonModule,
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
    PerfectScrollbarModule,
    JsonSchemaFormModule,
    MaterialDesignFrameworkModule,
  ],
  exports: [
    UploadZipfileDialogComponent,
    FileUploadComponent,
    NewFolderDialogComponent,
    NewThemeDialogComponent,
    NewSiteareaDialogComponent,
    NewContentDialogComponent,
    NewSiteConfigDialogComponent,
    NewLibraryDialogComponent,
  ],
})
export class WcmDialogModule {}
