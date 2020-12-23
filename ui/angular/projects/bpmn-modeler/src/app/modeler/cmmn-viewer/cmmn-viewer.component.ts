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
import { CmmnViewer } from "../../utils/cmmn-js";
import { importCmmnDiagram } from "../../utils/importCmmnDiagram";

@Component({
  selector: "cmmn-viewer",
  templateUrl: "./cmmn-viewer.component.html",
  styleUrls: ["./cmmn-viewer.component.scss"],
})
export class CmmnViewerComponent
  implements OnInit, AfterContentInit, OnDestroy {
  title = "Angular/CMMN";
  private viewer: any;
  private value = "";
  @ViewChild("ref", { static: true }) private el: ElementRef;
  @Output() private importDone: EventEmitter<any> = new EventEmitter();

  @Input() private url: string = "assets/cmmn/diagram.cmmn";
  // @Input() private url: string = "assets/cmmn/loan-approval.cmmn";

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.viewer = new CmmnViewer({
      // container: ".diagram-container",
      width: "100%",
      height: "600px",
    });

    this.viewer.on("import.done", ({ error }) => {
      console.log("imported");
      if (!error) {
        this.viewer.get("canvas").zoom("fit-viewport");
      } else {
        this._handleError(error);
      }
    });
  }

  //TODO: this should be ngAfterViewInit()
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
        importCmmnDiagram(this.viewer)
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
