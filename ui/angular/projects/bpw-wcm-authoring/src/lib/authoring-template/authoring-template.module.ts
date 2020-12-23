import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FlexLayoutModule } from "@angular/flex-layout";
import { RouterModule } from "@angular/router";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { CdkTableModule } from "@angular/cdk/table";
import { CdkTreeModule } from "@angular/cdk/tree";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatRippleModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { MatDividerModule } from "@angular/material/divider";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatListModule } from "@angular/material/list";
import { MatMenuModule } from "@angular/material/menu";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSelectModule } from "@angular/material/select";
import { MatSortModule } from "@angular/material/sort";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatTableModule } from "@angular/material/table";
import { MatTabsModule } from "@angular/material/tabs";
import { MatTreeModule } from "@angular/material/tree";
import { TranslateModule } from "@ngx-translate/core";
import { DragDropModule } from "@angular/cdk/drag-drop";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { JsonSchemaFormModule } from "@bpw/ajsf-core";
import { MaterialDesignFrameworkModule } from "@bpw/ajsf-material";
import { SharedUIModule, SidebarModule } from "bpw-common";

import { AuthoringTemplateListComponent } from "./authoring-template-list/authoring-template-list.component";
import { AuthoringTemplateEditorComponent } from "./authoring-template-editor/authoring-template-editor.component";
import { AuthoringTemplateLayoutComponent } from "./authoring-template-layout/authoring-template-layout.component";
import { AuthoringTemplateDialog } from "./authoring-template-layout/authoring-template-dialog.component";

import { AuthoringTemplateTreeComponent } from "./authoring-template-tree/authoring-template-tree.component";
import { ComponentModule } from "../components/component.module";
import { AclModule } from "../components/acl/acl.module";
import { HistoryModule } from "../components/history/history.module";
import { AuthoringTemplatePermissionComponent } from "./authoring-template-permission/authoring-template-permission.component";
import { AuthoringTemplateHistoryComponent } from "./authoring-template-history/authoring-template-history.component";
import { WcmFormWidgetModule } from "../wcm-form-widget/wcm-form-widget.module";
import { WcmFormGroupComponent } from "./form-group/form-group.component";
import { WcmFormColumnComponent } from "./form-column/form-column.component";
import { WcmResourceFieldDialog } from "./form-column/resource-field-dialog.component";
import { WcmStepEditorDialog } from "./form-group/step-editor-dialog.component";
import { WcmTabEditorDialog } from "./form-group/tab-editor-dialog.component";

@NgModule({
  declarations: [
    AuthoringTemplateListComponent,
    AuthoringTemplateEditorComponent,
    AuthoringTemplateLayoutComponent,
    AuthoringTemplateDialog,
    AuthoringTemplateTreeComponent,
    AuthoringTemplatePermissionComponent,
    AuthoringTemplateHistoryComponent,
    WcmFormGroupComponent,
    WcmFormColumnComponent,
    WcmResourceFieldDialog,
    WcmStepEditorDialog,
    WcmTabEditorDialog,
  ],
  imports: [
    RouterModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    ScrollingModule,
    CdkTableModule,
    CdkTreeModule,
    // NgxDnDModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatRippleModule,
    MatSelectModule,
    MatSortModule,
    MatTabsModule,
    MatTableModule,
    MatToolbarModule,
    MatTreeModule,

    PerfectScrollbarModule,
    DragDropModule,

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
  exports: [
    AuthoringTemplateListComponent,
    AuthoringTemplateEditorComponent,
    AuthoringTemplateLayoutComponent,
    AuthoringTemplateDialog,
    AuthoringTemplateTreeComponent,
    WcmFormGroupComponent,
    WcmFormColumnComponent,
    WcmResourceFieldDialog,
    WcmStepEditorDialog,
    WcmTabEditorDialog,
  ],
})
export class AuthoringTemplateModule {}
