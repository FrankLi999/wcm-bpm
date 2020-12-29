import { EventEmitter, Component, OnInit, Output } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import {
  BpmnModeler,
  bpmnPropertiesProviderModule,
  bpmnPropertiesPanelModule,
  camundaBpmnModdleDescriptor,
} from "../../utils/bpmn-js";
import { importBpmnDiagram } from "../../utils/importBpmnDiagram";

@Component({
  selector: "bpmn-modeler",
  templateUrl: "./bpmn-editor.component.html",
  styleUrls: ["./bpmn-editor.component.scss"],
})
export class BpmnEditorComponent implements OnInit {
  title = "Angular/BPMN";
  private modeler: any;
  @Output() private importDone: EventEmitter<any> = new EventEmitter();
  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.modeler = new BpmnModeler({
      container: "#canvas",
      width: "100%",
      height: "600px",
      additionalModules: [
        bpmnPropertiesPanelModule,
        bpmnPropertiesProviderModule,
      ],
      moddleExtensions: {
        camunda: camundaBpmnModdleDescriptor,
      },
      propertiesPanel: {
        parent: "#properties",
      },
    });
  }

  load(): void {
    const url = "/assets/bpmn/initial.bpmn";
    this.http
      .get(url, {
        headers: { observe: "response" },
        responseType: "text",
      })
      .pipe(
        catchError((err) => throwError(err)),
        importBpmnDiagram(this.modeler)
      )
      .subscribe(
        (warnings) => {
          this.importDone.emit({
            type: "success",
            warnings,
          });
        },
        (err) => {
          this._handleError(err);
          this.importDone.emit({
            type: "error",
            error: err,
          });
        }
      );
  }

  save(): void {
    this.modeler.saveXML((err: any, xml: any) =>
      console.log("Result of saving XML: ", err, xml)
    );
  }

  _handleError(err: any) {
    if (err) {
      console.warn("Ups, error: ", err);
    }
  }
}
