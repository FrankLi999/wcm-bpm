import { EventEmitter, Component, OnInit, Output } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import {
  CmmnModeler,
  cmmnPropertiesProviderModule,
  cmmnPropertiesPanelModule,
  camundaCmmnModdleDescriptor,
} from "../../utils/cmmn-js";
import { importCmmnDiagram } from "../../utils/importCmmnDiagram";

@Component({
  selector: "cmmn-modeler",
  templateUrl: "./cmmn-modeler.component.html",
  styleUrls: ["./cmmn-modeler.component.scss"],
})
export class CmmnModelerComponent implements OnInit {
  title = "Angular/CMMN";
  private modeler: any;
  @Output() private importDone: EventEmitter<any> = new EventEmitter();
  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.modeler = new CmmnModeler({
      container: "#canvas",
      width: "100%",
      height: "600px",
      additionalModules: [
        cmmnPropertiesPanelModule,
        cmmnPropertiesProviderModule,
      ],
      moddleExtensions: {
        camunda: camundaCmmnModdleDescriptor,
      },
      propertiesPanel: {
        parent: "#properties",
      },
    });
  }

  load(): void {
    const url = "/assets/cmmn/diagram.cmmn";
    this.http
      .get(url, {
        headers: { observe: "response" },
        responseType: "text",
      })
      .pipe(
        catchError((err) => throwError(err)),
        importCmmnDiagram(this.modeler)
      )
      .subscribe(
        (warnings) => {
          this.modeler.get("canvas").zoom("fit-viewport");
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
