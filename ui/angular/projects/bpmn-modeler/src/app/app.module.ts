import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HttpClientModule } from "@angular/common/http";
import { AppComponent } from "./app.component";
import { ModelerModule } from "./modeler/modeler.module";
import { BpmnDiagramComponent } from "./modeler/bpmn-diagram/bpmn-diagram.component";
import { BpmnEditorComponent } from "./modeler/bpmn-editor/bpmn-editor.component";
import { BpmnModelerComponent } from "./modeler/bpmn-modeler/bpmn-modeler.component";
import { BpmnViewerComponent } from "./modeler/bpmn-viewer/bpmn-viewer.component";
import { CmmnViewerComponent } from "./modeler/cmmn-viewer/cmmn-viewer.component";
import { CmmnModelerComponent } from "./modeler/cmmn-modeler/cmmn-modeler.component";
import { DmnViewerComponent } from "./modeler/dmn-viewer/dmn-viewer.component";
import { DmnModelerComponent } from "./modeler/dmn-modeler/dmn-modeler.component";
const appRoutes: Routes = [
  {
    path: "bpmn-diagram",
    component: BpmnDiagramComponent,
  },
  {
    path: "bpmn-editor",
    component: BpmnEditorComponent,
  },
  {
    path: "bpmn-modeler",
    component: BpmnModelerComponent,
  },
  {
    path: "bpmn-viewer",
    component: BpmnViewerComponent,
  },
  {
    path: "cmmn-modeler",
    component: CmmnModelerComponent,
  },
  {
    path: "cmmn-viewer",
    component: CmmnViewerComponent,
  },
  {
    path: "dmn-modeler",
    component: DmnModelerComponent,
  },
  {
    path: "dmn-viewer",
    component: DmnViewerComponent,
  },
  {
    path: "**",
    redirectTo: "bpmn-diagram",
  },
];

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    ModelerModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
