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
import { DmnViewer } from "../../utils/dmn-js";
import { importDmnDiagram } from "../../utils/importDmnDiagram";

@Component({
  selector: "app-dmn-viewer",
  templateUrl: "./dmn-viewer.component.html",
  styleUrls: ["./dmn-viewer.component.scss"],
})
export class DmnViewerComponent implements OnInit, AfterContentInit, OnDestroy {
  title = "Angular/DMN";
  private viewer: any;
  private value = "";
  @ViewChild("ref", { static: true }) private el: ElementRef;
  @Output() private importDone: EventEmitter<any> = new EventEmitter();

  @Input() private url: string = "assets/dmn/diagram13.dmn";
  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.viewer = new DmnViewer({
      width: "100%",
      height: "600px",
    });

    this.viewer.on("import.done", ({ error }) => {
      if (!error) {
        // this.viewer.getActiveViewer().get("canvas").zoom
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
        importDmnDiagram(this.viewer)
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
