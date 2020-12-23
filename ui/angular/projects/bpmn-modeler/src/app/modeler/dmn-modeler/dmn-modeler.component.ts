import {
  Component,
  ComponentFactoryResolver,
  ElementRef,
  ViewEncapsulation,
  EventEmitter,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
} from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { throwError, Subscription } from "rxjs";
import { catchError } from "rxjs/operators";
import {
  DmnModeler,
  dmnPropertiesProviderModule,
  dmnPropertiesPanelModule,
  camundaDmnModdleDescriptor,
  dmnDrdAdapterModule,
} from "../../utils/dmn-js";
import { importDmnDiagram } from "../../utils/importDmnDiagram";
import { DmnDiagramTabsComponent } from "../dmn-diagram-tabs/dmn-diagram-tabs.component";
import { DmnDiagramTabsHostDirective } from "../dmn-diagram-tabs-host.directive";
@Component({
  selector: "dmn-modeler",
  templateUrl: "./dmn-modeler.component.html",
  styleUrls: ["./dmn-modeler.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class DmnModelerComponent implements OnInit, OnDestroy {
  title = "Angular/DMN";
  @Output() private importDone: EventEmitter<any> = new EventEmitter();
  @ViewChild("ref", { static: true }) private el: ElementRef;
  private modeler: any;
  private dmnDiagramTabsCompFactory;
  private hostViewContainerRef;
  private tabsComponentRef;
  private tabClickSub: Subscription;
  @ViewChild(DmnDiagramTabsHostDirective, { static: true })
  tabsDirectiveHost: DmnDiagramTabsHostDirective;
  constructor(
    private http: HttpClient,
    private componentFactoryResolver: ComponentFactoryResolver
  ) {}

  ngOnInit(): void {
    this.modeler = new DmnModeler({
      drd: {
        width: "100%",
        height: "800px",
        propertiesPanel: {
          parent: "#properties",
        },
        additionalModules: [
          dmnPropertiesPanelModule,
          dmnPropertiesProviderModule,
          dmnDrdAdapterModule,
        ],
      },
      // container: "#canvas",
      width: "100%",
      height: "800px",
      keyboard: {
        bindTo: window,
      },

      moddleExtensions: {
        camunda: camundaDmnModdleDescriptor,
      },
    });

    this.modeler.on("import.done", ({ error, warnings }) => {
      if (!error) {
        const activeEditor = this.modeler.getActiveViewer();
        const canvas = activeEditor.get("canvas");
        canvas.zoom("fit-viewport");
        activeEditor.attachTo(this.el.nativeElement);

        const view = this.modeler.getViews()[0];
        console.log(view);
        this.modeler.open(view);

        this.dmnDiagramTabsCompFactory = this.componentFactoryResolver.resolveComponentFactory(
          DmnDiagramTabsComponent
        );
        this.hostViewContainerRef = this.tabsDirectiveHost.viewContainerRef;
        this.hostViewContainerRef.clear();
        this.tabsComponentRef = this.hostViewContainerRef.createComponent(
          this.dmnDiagramTabsCompFactory
        );
        this.tabsComponentRef.instance.views = this.modeler.getViews();
        this.tabClickSub = this.tabsComponentRef.instance.tabClick.subscribe(
          (aView) => {
            this.modeler.open(aView);
          }
        );
        this.modeler.on("import.render.complete", (aNewView, err, wrngs) => {
          const activeEditor1 = this.modeler.getActiveViewer();
          activeEditor1.attachTo(this.el.nativeElement);
        });
      }
    });
  }
  ngOnDestroy() {
    this.tabClickSub.unsubscribe();
  }
  load(): void {
    const url = "/assets/dmn/diagram13.dmn";
    this.http
      .get(url, {
        headers: { observe: "response" },
        responseType: "text",
      })
      .pipe(
        catchError((err) => throwError(err)),
        importDmnDiagram(this.modeler)
      )
      .subscribe(
        (warnings) => {
          this.modeler.getActiveViewer().get("canvas").zoom("fit-viewport");
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
