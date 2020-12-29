import { NgModule, CUSTOM_ELEMENTS_SCHEMA, Injector } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { MatIconModule } from "@angular/material/icon";
import { ChartsModule } from "ng2-charts";
import { FlexLayoutModule } from "@angular/flex-layout";
import { NgxChartsModule } from "@swimlane/ngx-charts";

import { SharedUIModule, WidgetModule } from "bpw-common";

import { createCustomElement } from "@angular/elements";
import { RunningInstancesComponent } from "./running-instances/running-instances.component";
import { OpenIncidentsComponent } from "./open-incidents/open-incidents.component";
import { HumanTasksComponent } from "./human-tasks/human-tasks.component";
import { ProcessDefinitionsComponent } from "./process-definitions/process-definitions.component";
import { CaseDefinitionsComponent } from "./case-definitions/case-definitions.component";
import { DecisionDefinitionsComponent } from "./decision-definitions/decision-definitions.component";
import { DeployedComponent } from "./deployed/deployed.component";

@NgModule({
  declarations: [
    CaseDefinitionsComponent,
    DecisionDefinitionsComponent,
    DeployedComponent,
    HumanTasksComponent,
    OpenIncidentsComponent,
    ProcessDefinitionsComponent,
    RunningInstancesComponent,
  ],
  imports: [
    ChartsModule,
    CommonModule,
    FlexLayoutModule,
    MatIconModule,
    NgxChartsModule,
    RouterModule,
    SharedUIModule,
    WidgetModule,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [
    CaseDefinitionsComponent,
    DecisionDefinitionsComponent,
    DeployedComponent,
    HumanTasksComponent,
    OpenIncidentsComponent,
    ProcessDefinitionsComponent,
    RunningInstancesComponent,
  ],
})
export class DashboardHomeModule {
  constructor(private injector: Injector) {
    const caseDefinitionsElement = createCustomElement(
      CaseDefinitionsComponent,
      { injector: injector }
    );
    const decisionDefinitionsElement = createCustomElement(
      DecisionDefinitionsComponent,
      { injector: injector }
    );
    const deployedElement = createCustomElement(DeployedComponent, {
      injector: injector,
    });
    const humanTasksElement = createCustomElement(HumanTasksComponent, {
      injector: injector,
    });
    const openIncidentsElement = createCustomElement(OpenIncidentsComponent, {
      injector: injector,
    });
    const processDefinitionsElement = createCustomElement(
      ProcessDefinitionsComponent,
      {
        injector: injector,
      }
    );
    const runningInstancesElement = createCustomElement(
      RunningInstancesComponent,
      {
        injector: injector,
      }
    );

    customElements.define("camunda-case-definitions", caseDefinitionsElement);
    customElements.define(
      "camunda-decision-definitions",
      decisionDefinitionsElement
    );
    customElements.define("camunda-deployed", deployedElement);
    customElements.define("camunda-human-tasks", humanTasksElement);
    customElements.define("camunda-open-incidents", openIncidentsElement);
    customElements.define(
      "camunda-process-definitions",
      processDefinitionsElement
    );
    customElements.define("camunda-running-instances", runningInstancesElement);
  }
}
