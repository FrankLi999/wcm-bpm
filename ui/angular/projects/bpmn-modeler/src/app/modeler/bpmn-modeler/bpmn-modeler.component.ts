import { EventEmitter, Component, OnInit, Output } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import {
  BpmnModeler,
  camundaBpmnModdleDescriptor,
  OriginalBpmnPropertiesProvider,
  bpmnPropertiesPanelModule,
  BpmnInjectionNames,
  OriginalBpmnPaletteProvider,
} from "../../utils/bpmn-js";
import { CustomBpmnPropsProvider } from "../../utils/CustomBpmnPropsProvider";
import { CustomBpmnPaletteProvider } from "../../utils/CustomBpmnPaletteProvider";
import { importBpmnDiagram } from "../../utils/importBpmnDiagram";
import customModdle from "../../utils/custom-bpmn.moddle.json";

@Component({
  selector: "bpmn-modeler",
  templateUrl: "./bpmn-modeler.component.html",
  styleUrls: ["./bpmn-modeler.component.scss"],
})
export class BpmnModelerComponent implements OnInit {
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

        // Re-use original bpmn-properties-module, see CustomPropsProvider
        {
          [BpmnInjectionNames.bpmnPropertiesProvider]: [
            "type",
            OriginalBpmnPropertiesProvider.propertiesProvider[1],
          ],
        },
        {
          [BpmnInjectionNames.propertiesProvider]: [
            "type",
            CustomBpmnPropsProvider,
          ],
        },

        // Re-use original palette, see CustomPaletteProvider
        {
          [BpmnInjectionNames.originalPaletteProvider]: [
            "type",
            OriginalBpmnPaletteProvider,
          ],
        },
        {
          [BpmnInjectionNames.paletteProvider]: [
            "type",
            CustomBpmnPaletteProvider,
          ],
        },
      ],
      propertiesPanel: {
        parent: "#properties",
      },
      moddleExtension: {
        camunda: camundaBpmnModdleDescriptor,
        custom: customModdle,
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

  private _handleError(err: any) {
    if (err) {
      console.warn("Ups, error: ", err);
    }
  }
}
