import { Injector, NgModule, CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { createCustomElement } from "@angular/elements";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatRippleModule } from "@angular/material/core";
import { MatExpansionModule } from "@angular/material/expansion";
import { CdkTableModule } from "@angular/cdk/table";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatInputModule } from "@angular/material/input";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatDialogModule } from "@angular/material/dialog";
import { MatMenuModule } from "@angular/material/menu";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatSelectModule } from "@angular/material/select";
import { MatIconModule } from "@angular/material/icon";
import { MatListModule } from "@angular/material/list";
import { MatTableModule } from "@angular/material/table";
import { FlexLayoutModule } from "@angular/flex-layout";
import { TranslateModule } from "@ngx-translate/core";
import { SharedUIModule, SidebarModule } from "bpw-common";

import { ManageAuthorizationsComponent } from "./authorizations/manage-authorizations/manage-authorizations.component";
import { AuthorizationTableComponent } from "./authorizations/authorization-table/authorization-table.component";
import { NewAuthorizationDialogComponent } from "./authorizations/new-authorization-dialog/new-authorization-dialog.component";
import { UserListComponent } from "./users/user-list/user-list.component";
import { EditUserComponent } from "./users/edit-user/edit-user.component";
import { NewGroupComponent } from "./groups/new-group/new-group.component";
import { GroupListComponent } from "./groups/group-list/group-list.component";
import { TenantListComponent } from "./tenants/tenant-list/tenant-list.component";
import { NewTenantComponent } from "./tenants/new-tenant/new-tenant.component";
import { ExecutionMetricsComponent } from "./system/execution-metrics/execution-metrics.component";
import { GeneralSettingsComponent } from "./system/general-settings/general-settings.component";

@NgModule({
  declarations: [
    ManageAuthorizationsComponent,
    AuthorizationTableComponent,
    NewAuthorizationDialogComponent,
    UserListComponent,
    EditUserComponent,
    NewGroupComponent,
    GroupListComponent,
    TenantListComponent,
    NewTenantComponent,
    ExecutionMetricsComponent,
    GeneralSettingsComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    FlexLayoutModule,
    CdkTableModule,
    MatCheckboxModule,
    MatInputModule,
    MatFormFieldModule,
    MatDialogModule,
    MatMenuModule,
    MatPaginatorModule,
    MatButtonModule,
    MatCardModule,
    MatRippleModule,
    MatExpansionModule,
    MatIconModule,
    MatListModule,
    MatSelectModule,
    MatTableModule,
    SharedUIModule,
    ScrollingModule,
    SidebarModule,
    TranslateModule,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [
    ManageAuthorizationsComponent,
    AuthorizationTableComponent,
    NewAuthorizationDialogComponent,
    UserListComponent,
    EditUserComponent,
    NewGroupComponent,
    GroupListComponent,
    TenantListComponent,
    NewTenantComponent,
    ExecutionMetricsComponent,
    GeneralSettingsComponent,
  ],
})
export class AdminModule {
  constructor(private injector: Injector) {
    const manageAuthorizationsElement = createCustomElement(
      ManageAuthorizationsComponent,
      { injector: injector }
    );
    customElements.define("manage-authorizations", manageAuthorizationsElement);
  }
}
