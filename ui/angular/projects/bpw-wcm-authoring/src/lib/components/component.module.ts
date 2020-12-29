import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FlexLayoutModule } from "@angular/flex-layout";
import { CdkTableModule } from "@angular/cdk/table";
import { CdkTreeModule } from "@angular/cdk/tree";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatRippleModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatListModule } from "@angular/material/list";
import { MatMenuModule } from "@angular/material/menu";
import { MatSelectModule } from "@angular/material/select";
import { MatToolbarModule } from "@angular/material/toolbar";

import { MatChipsModule } from "@angular/material/chips";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatSortModule } from "@angular/material/sort";
import { MatTableModule } from "@angular/material/table";
import { MatTabsModule } from "@angular/material/tabs";
import { MatTreeModule } from "@angular/material/tree";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { ResourceViewerComponent } from "./resource-viewer/resource-viewer.component";
import { SelectRenderTemplateDialog } from "./select-render-template/select-render-template.dialog";
import { SelectContentItemDialog } from "./select-content-item-dialog/select-content-item.dialog";
import { SharedUIModule, SidebarModule } from "bpw-common";
import { ContentSelectorComponent } from "./content-selector/content-selector.component";
import { WcmResponseComponent } from "./wcm-response/wcm-response.component";
import { WcmTreeComponent } from "./wcm-tree/wcm-tree/wcm-tree.component";
import { WcmExplorerComponent } from "./wcm-tree/wcm-explorer/wcm-explorer.component";

@NgModule({
  declarations: [
    SelectRenderTemplateDialog,
    SelectContentItemDialog,
    ResourceViewerComponent,
    ContentSelectorComponent,
    WcmResponseComponent,
    WcmTreeComponent,
    WcmExplorerComponent,
  ],
  exports: [
    SelectRenderTemplateDialog,
    SelectContentItemDialog,
    ResourceViewerComponent,
    ContentSelectorComponent,
    WcmResponseComponent,
    WcmTreeComponent,
    WcmExplorerComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    ScrollingModule,
    MatButtonModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDialogModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatRippleModule,
    MatPaginatorModule,
    MatSelectModule,
    MatSortModule,
    MatSnackBarModule,
    MatTabsModule,
    MatTableModule,
    MatToolbarModule,
    MatTreeModule,
    CdkTableModule,
    CdkTreeModule,
    PerfectScrollbarModule,
    SharedUIModule,
    SidebarModule,
  ],
})
export class ComponentModule {}
