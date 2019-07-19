import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BpmnComponent } from './bpmn/bpmn.component';
import { DiagramComponent } from './diagram/diagram.component';
const routes = [
  {
    path     : '**',
    component: BpmnComponent
  }
];

@NgModule({
  declarations: [
    BpmnComponent,
    DiagramComponent
  ],
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class BpmnModule { }