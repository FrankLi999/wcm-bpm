import { NgModule } from "@angular/core";
import { AdminModule } from ".//admin/admin.module";
import { DashboardModule } from "./dashboard/dashboard.module";
import { SiteHomeModule } from "./home/site-home.module";
import { UserListComponent } from "./admin/users/user-list/user-list.component";
import { EditUserComponent } from "./admin/users/edit-user/edit-user.component";
import { GroupListComponent } from "./admin/groups/group-list/group-list.component";
import { NewGroupComponent } from "./admin/groups/new-group/new-group.component";
import { TenantListComponent } from "./admin/tenants/tenant-list/tenant-list.component";
import { NewTenantComponent } from "./admin/tenants/new-tenant/new-tenant.component";
import { GeneralSettingsComponent } from "./admin/system/general-settings/general-settings.component";
import { ExecutionMetricsComponent } from "./admin/system/execution-metrics/execution-metrics.component";
import { ProcessesComponent } from "./dashboard/processes/processes/processes.component";
import { ProcessDefinitionRuntimeComponent } from "./dashboard/processes/process-definition-runtime/process-definition-runtime.component";
import { ProcessInstanceComponent } from "./dashboard/processes/process-instance/process-instance.component";
import { DeploymentsComponent } from "./dashboard/deployments/deployments/deployments.component";
import { CasesComponent } from "./dashboard/cases/cases/cases.component";
import { CaseDefinitionRuntimeComponent } from "./dashboard/cases/case-definition-runtime/case-definition-runtime.component";
import { DecisionsComponent } from "./dashboard/decisions/decisions//decisions.component";
import { DecisionDefinitionRuntimeComponent } from "./dashboard/decisions/decision-definition-runtime/decision-definition-runtime.component";
import { DecisionInstanceComponent } from "./dashboard/decisions/decision-instance/decision-instance.component";
import { IncidentsComponent } from "./dashboard/incidents/incidents/incidents.component";
import { AssignedByTypeComponent } from "./dashboard/tasks/assigned-by-type/assigned-by-type.component";
import { AssignedByGroupComponent } from "./dashboard/tasks/assigned-by-group/assigned-by-group.component";
import { TasklistModule } from "./tasklist/tasklist.module";
import { TasklistComponent } from "./tasklist/tasklist/tasklist.component";
import { appConfig } from "bpw-common";
// declare var appConfig: any;
appConfig.components = {
  UserListComponent: UserListComponent,
  EditUserComponent: EditUserComponent,
  NewGroupComponent: NewGroupComponent,
  GroupListComponent: GroupListComponent,
  NewTenantComponent: NewTenantComponent,
  TenantListComponent: TenantListComponent,
  GeneralSettingsComponent: GeneralSettingsComponent,
  ExecutionMetricsComponent: ExecutionMetricsComponent,
  DeploymentsComponent: DeploymentsComponent,
  ProcessInstanceComponent: ProcessInstanceComponent,
  ProcessDefinitionRuntimeComponent: ProcessDefinitionRuntimeComponent,
  ProcessesComponent: ProcessesComponent,
  CasesComponent: CasesComponent,
  CaseDefinitionRuntimeComponent: CaseDefinitionRuntimeComponent,
  DecisionsComponent: DecisionsComponent,
  DecisionDefinitionRuntimeComponent: DecisionDefinitionRuntimeComponent,
  DecisionInstanceComponent: DecisionInstanceComponent,
  IncidentsComponent: IncidentsComponent,
  AssignedByTypeComponent: AssignedByTypeComponent,
  AssignedByGroupComponent: AssignedByGroupComponent,
  TasklistComponent: TasklistComponent,
};
@NgModule({
  imports: [AdminModule, DashboardModule, SiteHomeModule, TasklistModule],
})
export class SiteModule {}
