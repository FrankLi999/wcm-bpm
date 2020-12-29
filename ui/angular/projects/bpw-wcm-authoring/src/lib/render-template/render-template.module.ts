import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FlexLayoutModule } from "@angular/flex-layout";
import { CdkTableModule } from "@angular/cdk/table";
import { CdkTreeModule } from "@angular/cdk/tree";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatRippleModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatMenuModule } from "@angular/material/menu";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
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
import { TranslateModule } from "@ngx-translate/core";
import { DragDropModule } from "@angular/cdk/drag-drop";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { SharedUIModule, SidebarModule } from "bpw-common";

import { RenderTemplateComponent } from "./render-template/render-template.component";
// import { AceEditorModule } from "ng2-ace-editor";
import { SafePipe } from "./pipes/safe.pipe";
import { RenderTemplatesComponent } from "./render-templates/render-templates.component";
import { RenderLayoutDesignerComponent } from "./render-layout-designer/render-layout-designer.component";
import { RenderTemplateTreeComponent } from "./render-template-tree/render-template-tree.component";
import { RenderTemplateHistoryComponent } from "./render-template-history/render-template-history.component";
import { RenderTemplatePermissionsComponent } from "./render-template-permissions/render-template-permissions.component";
import { ComponentModule } from "../components/component.module";
import { AclModule } from "../components/acl/acl.module";
import { HistoryModule } from "../components/history/history.module";
import { WcmFormWidgetModule } from "../wcm-form-widget/wcm-form-widget.module";

@NgModule({
  declarations: [
    RenderTemplateComponent,
    SafePipe,
    RenderTemplatesComponent,
    RenderLayoutDesignerComponent,
    RenderTemplateTreeComponent,
    RenderTemplateHistoryComponent,
    RenderTemplatePermissionsComponent,
  ],
  imports: [
    RouterModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    // AceEditorModule,
    CdkTableModule,
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
    MatPaginatorModule,
    MatRippleModule,
    MatSelectModule,
    MatToolbarModule,
    MatChipsModule,
    MatExpansionModule,
    MatSortModule,
    MatSnackBarModule,
    MatTableModule,
    MatTabsModule,
    MatTreeModule,
    DragDropModule,
    ScrollingModule,
    PerfectScrollbarModule,
    TranslateModule.forChild({
      extend: true,
    }),
    SharedUIModule,
    SidebarModule,
    ComponentModule,
    AclModule,
    HistoryModule,
    WcmFormWidgetModule,
  ],
  exports: [
    RenderTemplateComponent,
    SafePipe,
    RenderTemplatesComponent,
    RenderLayoutDesignerComponent,
    RenderTemplateTreeComponent,
  ],
})
export class RenderTemplateModule {}
