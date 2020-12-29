import { NgModule } from "@angular/core";

import { CasesModule } from "./cases/cases.module";
import { DashboardHomeModule } from "./dashboard/dashboard-home.module";
import { DecisionsModule } from "./decisions/decisions.module";
import { DeploymentsModule } from "./deployments/deployments.module";
import { IncidentsModule } from "./incidents/incidents.module";
import { ProcessesModule } from "./processes/processes.module";
import { TasksModule } from "./tasks/tasks.module";

@NgModule({
  imports: [
    CasesModule,
    DashboardHomeModule,
    DecisionsModule,
    DeploymentsModule,
    IncidentsModule,
    ProcessesModule,
    TasksModule,
  ],
})
export class DashboardModule {}
