import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { FlexLayoutModule } from "@angular/flex-layout";
import { CdkTableModule } from "@angular/cdk/table";
import { CdkTreeModule } from "@angular/cdk/tree";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatRippleModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
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
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";

import { SharedUIModule, SidebarModule } from "bpw-common";
import { JsonSchemaFormModule } from "@bpw/ajsf-core";
import { MaterialDesignFrameworkModule } from "@bpw/ajsf-material";
import { SiteExplorerComponent } from "./site-explorer/site-explorer.component";
import { FolderOverviewComponent } from "./folder-overview/folder-overview.component";
import { SelectAuthoringTemplateDialogComponent } from "./select-authoring-template-dialog/select-authoring-template-dialog.component";
import { AuthoringTemplateSelectorComponent } from "./authoring-template-selector/authoring-template-selector.component";
import { ContentItemComponent } from "./content-item/content-item.component";
import { SiteAreaComponent } from "./site-area/site-area.component";
import { SiteAreaLayoutComponent } from "./site-area-layout/site-area-layout.component";
import { SiteTreeComponent } from "./site-tree/site-tree.component";
import { ComponentModule } from "../components/component.module";
import { ItemPermissionsComponent } from "./item-permissions/item-permissions.component";
import { ItemHistoryComponent } from "./item-history/item-history.component";
import { AclModule } from "../components/acl/acl.module";
import { HistoryModule } from "../components/history/history.module";
import { WcmEditableComponent } from "./wcm-editable/wcm-editable.component";
import { EditableOnEnterDirective } from "./wcm-editable/editable-on-enter.directive";
import { ViewModeDirective } from "./wcm-editable/view-mode.directive";
import { EditModeDirective } from "./wcm-editable/edit-mode.directive";
import { ContentAreaPreviewComponent } from "./content-area-preview/content-area-preview.component";
import { WcmRenderingModule } from "bpw-wcm-rendering";
import { InPlaceContentItemEditorComponent } from "./in-place-content-item-editor/in-place-content-item-editor.component";
import { WcmFormWidgetModule } from "../wcm-form-widget/wcm-form-widget.module";

@NgModule({
  declarations: [
    ContentAreaPreviewComponent,
    WcmEditableComponent,
    EditableOnEnterDirective,
    ViewModeDirective,
    EditModeDirective,
    SiteExplorerComponent,
    FolderOverviewComponent,
    SelectAuthoringTemplateDialogComponent,
    AuthoringTemplateSelectorComponent,
    ContentItemComponent,
    SiteAreaComponent,
    SiteAreaLayoutComponent,
    SiteTreeComponent,
    ItemPermissionsComponent,
    ItemHistoryComponent,
    InPlaceContentItemEditorComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    FlexLayoutModule,
    CdkTableModule,
    CdkTreeModule,
    ScrollingModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatDialogModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
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
    WcmRenderingModule,
    WcmFormWidgetModule,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [
    ContentAreaPreviewComponent,
    WcmEditableComponent,
    EditableOnEnterDirective,
    ViewModeDirective,
    EditModeDirective,
    SiteExplorerComponent,
    FolderOverviewComponent,
    SelectAuthoringTemplateDialogComponent,
    AuthoringTemplateSelectorComponent,
    ContentItemComponent,
    SiteAreaComponent,
    SiteAreaLayoutComponent,
    SiteTreeComponent,
  ],
})
export class SiteExplorerModule {}
