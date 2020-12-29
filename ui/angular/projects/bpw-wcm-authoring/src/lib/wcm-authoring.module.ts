import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
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

import { TranslateModule } from "@ngx-translate/core";
// import { AceEditorModule } from "ng2-ace-editor";
import { WcmDialogModule } from "./dialog/wcm-dialog.module";
import {
  SharedUIModule,
  ProgressBarModule,
  SidebarModule,
  ThemeOptionsModule,
  LayoutModule,
  AuthGuard,
  AuthStoreModule,
} from "bpw-common";
import { JsonSchemaFormModule } from "@bpw/ajsf-core";
import { MaterialDesignFrameworkModule } from "@bpw/ajsf-material";
import { ResolveForAuthoringGuard, WcmAppStoreModule } from "bpw-wcm-service";
import { ComponentModule } from "./components/component.module";
import { JcrExplorerModule } from "./jcr-explorer/jcr-explorer.module";
import { ResourceLibraryModule } from "./resource-library/resource-library.module";
import { SiteExplorerModule } from "./site-explorer/site-explorer.module";
import { ContentAreaLayoutModule } from "./content-area-layout/content-area-layout.module";
import { AuthoringTemplateModule } from "./authoring-template/authoring-template.module";
import { RenderTemplateModule } from "./render-template/render-template.module";
import { QueryBuilderModule } from "./query-builder/query-builder.module";
import { CategoryModule } from "./category/category.module";
import { SiteConfigModule } from "./site-config/site-config.module";
import { WorkflowModule } from "./workflow/workflow.module";
import { ValidationRuleModule } from "./validation-rule/validation-rule.module";
import { WcmRenderingModule } from "bpw-wcm-rendering";

import { WcmAuthoringComponent } from "./entry/wcm-authoring/wcm-authoring.component";
import { JcrExplorerComponent } from "./jcr-explorer/jcr-explorer/jcr-explorer.component";
import { CategoryTreeComponent } from "./category/category-tree/category-tree.component";
import { CategoryComponent } from "./category/category/category.component";
import { CategoryPermissionsComponent } from "./category/category-permissions/category-permissions.component";
import { ContentAreaLayoutTreeComponent } from "./content-area-layout/content-area-layout-tree/content-area-layout-tree.component";
import { ContentAreaLayoutsComponent } from "./content-area-layout/content-area-layouts/content-area-layouts.component";
import { ContentAreaDesignerComponent } from "./content-area-layout/content-area-designer/content-area-designer.component";
import { ContentAreaPreviewComponent } from "./site-explorer/content-area-preview/content-area-preview.component";
import { ContentAreaLayoutHistoryComponent } from "./content-area-layout/content-area-layout-history/content-area-layout-history.component";
import { ContentAreaLayoutPermissionsComponent } from "./content-area-layout/content-area-layout-permissions/content-area-layout-permissions.component";
import { QueryTreeComponent } from "./query-builder/query-tree/query-tree.component";
import { QueryStatementComponent } from "./query-builder/query-statement/query-statement.component";
import { QueryListComponent } from "./query-builder/query-list/query-list.component";
import { QueryHistoryComponent } from "./query-builder/query-history/query-history.component";
import { QueryPermissionsComponent } from "./query-builder/query-permissions/query-permissions.component";
import { RenderTemplateTreeComponent } from "./render-template/render-template-tree/render-template-tree.component";
import { RenderTemplateComponent } from "./render-template/render-template/render-template.component";
import { RenderTemplateHistoryComponent } from "./render-template/render-template-history/render-template-history.component";
import { RenderTemplatesComponent } from "./render-template/render-templates/render-templates.component";
import { RenderTemplatePermissionsComponent } from "./render-template/render-template-permissions/render-template-permissions.component";
import { ResourceLibraryTreeComponent } from "./resource-library/resource-library-tree/resource-library-tree.component";
import { LibraryComponent } from "./resource-library/library/library.component";
import { LibrariesComponent } from "./resource-library/libraries/libraries.component";
import { LibraryPermissionsComponent } from "./resource-library/library-permissions/library-permissions.component";
import { LibraryHistoryComponent } from "./resource-library/library-history/library-history.component";
import { AuthoringTemplateTreeComponent } from "./authoring-template/authoring-template-tree/authoring-template-tree.component";
import { AuthoringTemplateListComponent } from "./authoring-template/authoring-template-list/authoring-template-list.component";
import { AuthoringTemplateEditorComponent } from "./authoring-template/authoring-template-editor/authoring-template-editor.component";
import { AuthoringTemplateHistoryComponent } from "./authoring-template/authoring-template-history/authoring-template-history.component";
import { AuthoringTemplatePermissionComponent } from "./authoring-template/authoring-template-permission/authoring-template-permission.component";
import { SiteTreeComponent } from "./site-explorer/site-tree/site-tree.component";
import { SiteAreaComponent } from "./site-explorer/site-area/site-area.component";
import { ItemPermissionsComponent } from "./site-explorer/item-permissions/item-permissions.component";
import { ItemHistoryComponent } from "./site-explorer/item-history/item-history.component";
import { ContentItemComponent } from "./site-explorer/content-item/content-item.component";
import { SiteExplorerComponent } from "./site-explorer/site-explorer/site-explorer.component";
import { SiteConfigComponent } from "./site-config/site-config/site-config.component";
import { SiteConfigsComponent } from "./site-config/site-configs/site-configs.component";
import { SiteConfigTreeComponent } from "./site-config/site-config-tree/site-config-tree.component";
import { ValidationRuleTreeComponent } from "./validation-rule/validation-rule-tree/validation-rule-tree.component";
import { ValidationRulesComponent } from "./validation-rule/validation-rules/validation-rules.component";
import { ValidationRuleComponent } from "./validation-rule/validation-rule/validation-rule.component";
import { ValidationRulePermissionsComponent } from "./validation-rule/validation-rule-permissions/validation-rule-permissions.component";
import { ValidationRuleHistoryComponent } from "./validation-rule/validation-rule-history/validation-rule-history.component";
import { WorkflowTreeComponent } from "./workflow/workflow-tree/workflow-tree.component";
import { WorkflowsComponent } from "./workflow/workflows/workflows.component";
import { WorkflowComponent } from "./workflow/workflow/workflow.component";
import { WorkflowHistoryComponent } from "./workflow/workflow-history/workflow-history.component";
import { WorkflowPermissionsComponent } from "./workflow/workflow-permissions/workflow-permissions.component";
import { FormDesignerModule } from "./form-designer/form-designer.module";
import { FormDesignerTreeComponent } from "./form-designer/form-designer-tree/form-designer-tree.component";
import { FormEditorComponent } from "./form-designer/form-editor/form-editor.component";
import { FormListComponent } from "./form-designer/form-list/form-list.component";
import { FormHistoryComponent } from "./form-designer/form-history/form-history.component";
import { FormPermissionComponent } from "./form-designer/form-permission/form-permission.component";
import { AuthoringTaskModule } from "./authoring-task/authoring-task.module";
import { DraftItemTreeComponent } from "./authoring-task/draft-item-tree/draft-item-tree.component";
import { ContentApprovalComponent } from "./authoring-task/content-approval/content-approval.component";
import { RejectDraftComponent } from "./authoring-task/reject-draft/reject-draft.component";
const routes: Routes = [
  {
    path: "",
    component: WcmAuthoringComponent,
    canActivate: [AuthGuard, ResolveForAuthoringGuard],
    children: [
      {
        path: "jcr-explorer",
        component: JcrExplorerComponent,
      },
      {
        path: "category",
        component: CategoryTreeComponent,
        children: [
          {
            path: "item",
            component: CategoryComponent,
          },
          {
            path: "edit-permissions",
            component: CategoryPermissionsComponent,
          },
        ],
      },
      {
        path: "content-area-layout",
        component: ContentAreaLayoutTreeComponent,
        children: [
          {
            path: "edit",
            component: ContentAreaDesignerComponent,
          },
          {
            path: "new",
            component: ContentAreaDesignerComponent,
          },
          {
            path: "list",
            component: ContentAreaLayoutsComponent,
          },
          {
            path: "show-history",
            component: ContentAreaLayoutHistoryComponent,
          },
          {
            path: "edit-permissions",
            component: ContentAreaLayoutPermissionsComponent,
          },
        ],
      },
      {
        path: "preview",
        component: ContentAreaPreviewComponent,
      },
      {
        path: "query-builder",
        component: QueryTreeComponent,
        children: [
          {
            path: "edit",
            component: QueryStatementComponent,
          },
          {
            path: "new",
            component: QueryStatementComponent,
          },
          {
            path: "list",
            component: QueryListComponent,
          },
          {
            path: "edit-permissions",
            component: QueryPermissionsComponent,
          },
          {
            path: "show-history",
            component: QueryHistoryComponent,
          },
        ],
      },
      {
        path: "render-template",
        component: RenderTemplateTreeComponent,
        children: [
          {
            path: "edit",
            component: RenderTemplateComponent,
          },
          {
            path: "new",
            component: RenderTemplateComponent,
          },
          {
            path: "list",
            component: RenderTemplatesComponent,
          },
          {
            path: "edit-permissions",
            component: RenderTemplatePermissionsComponent,
          },
          {
            path: "show-history",
            component: RenderTemplateHistoryComponent,
          },
        ],
      },
      {
        path: "resource-library",
        component: ResourceLibraryTreeComponent,
        children: [
          {
            path: "edit",
            component: LibraryComponent,
          },
          {
            path: "new",
            component: LibraryComponent,
          },
          {
            path: "edit-permissions",
            component: LibraryPermissionsComponent,
          },
          {
            path: "show-history",
            component: LibraryHistoryComponent,
          },
          {
            path: "list",
            component: LibrariesComponent,
          },
        ],
      },
      {
        path: "form-designer",
        component: FormDesignerTreeComponent,
        children: [
          {
            path: "edit",
            component: FormEditorComponent,
          },
          {
            path: "new",
            component: FormEditorComponent,
          },
          {
            path: "list",
            component: FormListComponent,
          },
          {
            path: "edit-permissions",
            component: FormPermissionComponent,
          },
          {
            path: "show-history",
            component: FormHistoryComponent,
          },
        ],
      },
      {
        path: "authoring-template",
        component: AuthoringTemplateTreeComponent,
        children: [
          {
            path: "edit",
            component: AuthoringTemplateEditorComponent,
          },
          {
            path: "new",
            component: AuthoringTemplateEditorComponent,
          },
          {
            path: "list",
            component: AuthoringTemplateListComponent,
          },
          {
            path: "edit-permissions",
            component: AuthoringTemplatePermissionComponent,
          },
          {
            path: "show-history",
            component: AuthoringTemplateHistoryComponent,
          },
        ],
      },
      {
        path: "site-config",
        component: SiteConfigTreeComponent,
        children: [
          {
            path: "list",
            component: SiteConfigsComponent,
          },
          {
            path: "new",
            component: SiteConfigComponent,
          },
          {
            path: "edit",
            component: SiteConfigComponent,
          },
        ],
      },
      {
        path: "site-explorer",
        component: SiteTreeComponent,
        children: [
          {
            path: "navigator",
            component: SiteExplorerComponent,
          },
          {
            path: "new-content",
            component: ContentItemComponent,
          },
          {
            path: "edit-content",
            component: ContentItemComponent,
          },
          {
            path: "new-sa",
            component: SiteAreaComponent,
          },
          {
            path: "edit-sa",
            component: SiteAreaComponent,
          },
          {
            path: "show-history",
            component: ItemHistoryComponent,
          },
          {
            path: "edit-permissions",
            component: ItemPermissionsComponent,
          },
        ],
      },
      {
        path: "validation-rule",
        component: ValidationRuleTreeComponent,
        children: [
          {
            path: "edit",
            component: ValidationRuleComponent,
          },
          {
            path: "new",
            component: ValidationRuleComponent,
          },
          {
            path: "list",
            component: ValidationRulesComponent,
          },
          {
            path: "edit-permissions",
            component: ValidationRulePermissionsComponent,
          },
          {
            path: "show-history",
            component: ValidationRuleHistoryComponent,
          },
        ],
      },
      {
        path: "review-tasks",
        component: DraftItemTreeComponent,
      },
      {
        path: "approve-draft",
        component: ContentApprovalComponent,
      },
      {
        path: "reject-draft",
        component: RejectDraftComponent,
      },
      {
        path: "workflow",
        component: WorkflowTreeComponent,
        children: [
          {
            path: "edit",
            component: WorkflowComponent,
          },
          {
            path: "new",
            component: WorkflowComponent,
          },
          {
            path: "list",
            component: WorkflowsComponent,
          },
          {
            path: "edit-permissions",
            component: WorkflowPermissionsComponent,
          },
          {
            path: "show-history",
            component: WorkflowHistoryComponent,
          },
        ],
      },
    ],
  },
];
@NgModule({
  declarations: [WcmAuthoringComponent],
  imports: [
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
    WcmDialogModule,
    JsonSchemaFormModule,
    MaterialDesignFrameworkModule,
    TranslateModule.forChild({
      extend: true,
    }),

    SharedUIModule,
    SidebarModule,
    // AceEditorModule,

    ProgressBarModule,
    ThemeOptionsModule,

    LayoutModule,

    AuthStoreModule,
    WcmAppStoreModule,
    ComponentModule,
    JcrExplorerModule,
    ResourceLibraryModule,
    SiteExplorerModule,
    ContentAreaLayoutModule,
    AuthoringTemplateModule,
    RenderTemplateModule,
    QueryBuilderModule,
    CategoryModule,
    WorkflowModule,
    SiteConfigModule,
    ValidationRuleModule,
    WcmRenderingModule,
    FormDesignerModule,
    AuthoringTaskModule,
  ],
  providers: [ResolveForAuthoringGuard],
  exports: [WcmAuthoringComponent],
})
export class WcmAuthoringModule {}
