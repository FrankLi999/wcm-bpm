import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { BpmnDiagramComponent } from "./bpmn-diagram/bpmn-diagram.component";
import { BpmnEditorComponent } from "./bpmn-editor/bpmn-editor.component";
import { DiagramComponent } from "./diagram/diagram.component";
import { BpmnModelerComponent } from "./bpmn-modeler/bpmn-modeler.component";
import { BpmnViewerComponent } from "./bpmn-viewer/bpmn-viewer.component";
import { CmmnViewerComponent } from "./cmmn-viewer/cmmn-viewer.component";
import { DmnViewerComponent } from "./dmn-viewer/dmn-viewer.component";
import { CmmnModelerComponent } from "./cmmn-modeler/cmmn-modeler.component";
import { DmnModelerComponent } from "./dmn-modeler/dmn-modeler.component";
import { DmnDiagramTabsComponent } from "./dmn-diagram-tabs/dmn-diagram-tabs.component";
import { DmnDiagramTabsHostDirective } from "./dmn-diagram-tabs-host.directive";

@NgModule({
  declarations: [
    BpmnDiagramComponent,
    BpmnEditorComponent,
    DiagramComponent,
    BpmnModelerComponent,
    BpmnViewerComponent,
    CmmnViewerComponent,
    DmnViewerComponent,
    CmmnModelerComponent,
    DmnModelerComponent,
    DmnDiagramTabsComponent,
    DmnDiagramTabsHostDirective,
  ],
  imports: [CommonModule],
  exports: [
    BpmnDiagramComponent,
    BpmnEditorComponent,
    DiagramComponent,
    BpmnModelerComponent,
    BpmnViewerComponent,
    CmmnViewerComponent,
    DmnViewerComponent,
    CmmnModelerComponent,
    DmnModelerComponent,
    DmnDiagramTabsComponent,
    DmnDiagramTabsHostDirective,
  ],
})
export class ModelerModule {}
