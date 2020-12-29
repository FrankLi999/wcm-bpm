import {
  AfterContentInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnInit,
  OnDestroy,
  Output,
  ViewChild,
} from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { BpmnViewer } from "../../utils/bpmn-js";
import { importBpmnDiagram } from "../../utils/importBpmnDiagram";

@Component({
  selector: "bpmn-viewer",
  templateUrl: "./bpmn-viewer.component.html",
  styleUrls: ["./bpmn-viewer.component.scss"],
})
export class BpmnViewerComponent
  implements OnInit, AfterContentInit, OnDestroy {
  title = "Angular/BPMN";
  private viewer: any;
  private value = "";
  @ViewChild("ref", { static: true }) private el: ElementRef;
  @Output() private importDone: EventEmitter<any> = new EventEmitter();

  @Input() private url: string = "assets/pizza-collaboration.bpmn";
  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.viewer = new BpmnViewer({
      width: "100%",
      height: "600px",
    });

    this.viewer.on("import.done", ({ error }) => {
      if (!error) {
        this.viewer.get("canvas").zoom("fit-viewport");
      } else {
        this._handleError(error);
      }
    });
  }

  ngAfterContentInit(): void {
    this.viewer.attachTo(this.el.nativeElement);
    this._loadUrl(this.url);
  }

  ngOnDestroy(): void {
    this.viewer.destroy();
  }

  onInputChange(event: any) {
    this.value = event.target.value;
  }

  load() {
    this.url = this.value;
    this._loadUrl(this.url);
  }

  _handleError(err: any) {
    if (err) {
      console.warn("Ups, error: ", err);
    }
  }

  _loadUrl(url: string) {
    return this.http
      .get(url, { responseType: "text" })
      .pipe(
        catchError((err) => throwError(err)),
        importBpmnDiagram(this.viewer)
      )
      .subscribe(
        (warnings) => {
          this.importDone.emit({
            type: "success",
            warnings,
          });
        },
        (err) => {
          this.importDone.emit({
            type: "error",
            error: err,
          });
        }
      );
  }
}
