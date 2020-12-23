import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { FlexLayoutModule } from "@angular/flex-layout";
import { CdkTreeModule } from "@angular/cdk/tree";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatRippleModule } from "@angular/material/core";
import { MatCardModule } from "@angular/material/card";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatMenuModule } from "@angular/material/menu";
import { MatSelectModule } from "@angular/material/select";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatTreeModule } from "@angular/material/tree";
import { MatChipsModule } from "@angular/material/chips";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatSortModule } from "@angular/material/sort";
import { MatTableModule } from "@angular/material/table";
import { MatTabsModule } from "@angular/material/tabs";
import { TranslateModule } from "@ngx-translate/core";

import { SharedUIModule, SidebarModule } from "bpw-common";

import { LibrariesComponent } from "./libraries/libraries.component";
import { LibraryComponent } from "./library/library.component";
import { ResourceLibraryTreeComponent } from "./resource-library-tree/resource-library-tree.component";
import { JsonSchemaFormModule } from "@bpw/ajsf-core";
import { MaterialDesignFrameworkModule } from "@bpw/ajsf-material";
import { ComponentModule } from "../components/component.module";
import { AclModule } from "../components/acl/acl.module";
import { HistoryModule } from "../components/history/history.module";
import { LibraryPermissionsComponent } from "./library-permissions/library-permissions.component";
import { LibraryHistoryComponent } from "./library-history/library-history.component";
import { WcmFormWidgetModule } from "../wcm-form-widget/wcm-form-widget.module";

@NgModule({
  declarations: [
    LibrariesComponent,
    LibraryComponent,
    ResourceLibraryTreeComponent,
    LibraryPermissionsComponent,
    LibraryHistoryComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    CdkTreeModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatProgressSpinnerModule,
    MatRippleModule,
    MatSelectModule,
    MatToolbarModule,
    MatChipsModule,
    MatExpansionModule,
    MatPaginatorModule,
    MatSortModule,
    MatSnackBarModule,
    MatTableModule,
    MatTabsModule,
    MatTreeModule,
    TranslateModule.forChild({
      extend: true,
    }),
    SharedUIModule,
    SidebarModule,
    JsonSchemaFormModule,
    MaterialDesignFrameworkModule,
    ComponentModule,
    AclModule,
    HistoryModule,
    WcmFormWidgetModule,
  ],
  exports: [LibrariesComponent, LibraryComponent, ResourceLibraryTreeComponent],
})
export class ResourceLibraryModule {}
