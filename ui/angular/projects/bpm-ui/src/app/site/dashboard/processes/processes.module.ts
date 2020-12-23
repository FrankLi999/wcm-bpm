import { NgModule, CUSTOM_ELEMENTS_SCHEMA, Injector } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { FlexLayoutModule } from "@angular/flex-layout";
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
import { MatSortModule } from "@angular/material/sort";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSelectModule } from "@angular/material/select";
import { MatIconModule } from "@angular/material/icon";
import { MatListModule } from "@angular/material/list";
import { MatTableModule } from "@angular/material/table";

import { TranslateModule } from "@ngx-translate/core";
import { MatTabsModule } from "@angular/material/tabs";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { SharedUIModule, SidebarModule } from "bpw-common";
import { ProcessesComponent } from "./processes/processes.component";
import { ProcessDefinitionRuntimeComponent } from "./process-definition-runtime/process-definition-runtime.component";
import { ProcessInstanceComponent } from "./process-instance/process-instance.component";
import { ProcessInstanceTableComponent } from "./process-definition-runtime/process-instance-table/process-instance-table.component";
import { IncidentTableComponent } from "./process-definition-runtime/incident-table/incident-table.component";

@NgModule({
  declarations: [
    ProcessesComponent,
    ProcessDefinitionRuntimeComponent,
    ProcessInstanceComponent,
    ProcessInstanceTableComponent,
    IncidentTableComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    MatIconModule,
    FlexLayoutModule,
    MatTabsModule,
    FlexLayoutModule,
    CdkTableModule,
    MatCardModule,
    MatCheckboxModule,
    MatInputModule,
    MatFormFieldModule,
    MatDialogModule,
    MatMenuModule,
    MatPaginatorModule,
    MatButtonModule,
    MatRippleModule,
    MatExpansionModule,
    MatIconModule,
    MatListModule,
    MatProgressSpinnerModule,
    MatSelectModule,
    MatSortModule,
    MatTableModule,
    SharedUIModule,
    SidebarModule,
    TranslateModule,
    PerfectScrollbarModule,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [
    ProcessesComponent,
    ProcessDefinitionRuntimeComponent,
    ProcessInstanceComponent,
  ],
})
export class ProcessesModule {}
