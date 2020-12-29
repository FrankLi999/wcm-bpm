import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FlexLayoutModule } from "@angular/flex-layout";
import { CdkTableModule } from "@angular/cdk/table";
import { CdkTreeModule } from "@angular/cdk/tree";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatRippleModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatMenuModule } from "@angular/material/menu";
import { MatSelectModule } from "@angular/material/select";
import { MatToolbarModule } from "@angular/material/toolbar";

import { MatChipsModule } from "@angular/material/chips";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatSortModule } from "@angular/material/sort";
import { MatTableModule } from "@angular/material/table";
import { MatTabsModule } from "@angular/material/tabs";
import { MatTreeModule } from "@angular/material/tree";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { TranslateModule } from "@ngx-translate/core";

import { ValidationRuleComponent } from "./validation-rule/validation-rule.component";
import { ValidationRulesComponent } from "./validation-rules/validation-rules.component";
import { ValidationRuleTreeComponent } from "./validation-rule-tree/validation-rule-tree.component";
import { SharedUIModule, SidebarModule } from "bpw-common";
import { JsonSchemaFormModule } from "@bpw/ajsf-core";
import { MaterialDesignFrameworkModule } from "@bpw/ajsf-material";
import { ValidationRulePermissionsComponent } from "./validation-rule-permissions/validation-rule-permissions.component";
import { ValidationRuleHistoryComponent } from "./validation-rule-history/validation-rule-history.component";
import { AclModule } from "../components/acl/acl.module";
import { HistoryModule } from "../components/history/history.module";
import { ComponentModule } from "../components/component.module";
import { WcmFormWidgetModule } from "../wcm-form-widget/wcm-form-widget.module";

@NgModule({
  declarations: [
    ValidationRuleComponent,
    ValidationRulesComponent,
    ValidationRuleTreeComponent,
    ValidationRulePermissionsComponent,
    ValidationRuleHistoryComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    CdkTableModule,
    CdkTreeModule,
    ScrollingModule,
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
    PerfectScrollbarModule,
    TranslateModule.forChild({
      extend: true,
    }),
    SharedUIModule,
    SidebarModule,
    JsonSchemaFormModule,
    MaterialDesignFrameworkModule,
    AclModule,
    HistoryModule,
    ComponentModule,
    WcmFormWidgetModule,
  ],
  exports: [
    ValidationRuleComponent,
    ValidationRulesComponent,
    ValidationRuleTreeComponent,
  ],
})
export class ValidationRuleModule {}
