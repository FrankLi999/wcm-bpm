import { Component, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'bpmn',
  templateUrl: './bpmn.component.html',
  styleUrls: ['./bpmn.component.scss']
  // encapsulation: ViewEncapsulation.None
})
export class BpmnComponent {
  title = 'bpmn';
  diagramUrl = 'https://cdn.rawgit.com/bpmn-io/bpmn-js-examples/dfceecba/starter/diagram.bpmn';
  importError?: Error;

  handleImported(event) {
    const {
      type,
      error,
      warnings
    } = event;

    if (type === 'success') {
      console.log(`Rendered diagram (%s warnings)`, warnings.length);
    }

    if (type === 'error') {
      console.error('Failed to render diagram', error);
    }

    this.importError = error;
  }

}
