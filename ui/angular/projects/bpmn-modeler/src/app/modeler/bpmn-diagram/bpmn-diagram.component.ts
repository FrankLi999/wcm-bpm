import { Component, OnInit } from "@angular/core";

@Component({
  selector: "app-bpmn-diagram",
  templateUrl: "./bpmn-diagram.component.html",
  styleUrls: ["./bpmn-diagram.component.scss"],
})
export class BpmnDiagramComponent implements OnInit {
  title = "bpmn-js-angular";
  diagramUrl =
    "https://cdn.staticaly.com/gh/bpmn-io/bpmn-js-examples/dfceecba/starter/diagram.bpmn";
  importError?: Error;
  constructor() {}

  ngOnInit(): void {}
  handleImported(event) {
    const { type, error, warnings } = event;

    if (type === "success") {
      console.log(`Rendered diagram (%s warnings)`, warnings.length);
    }

    if (type === "error") {
      console.error("Failed to render diagram", error);
    }

    this.importError = error;
  }
}
