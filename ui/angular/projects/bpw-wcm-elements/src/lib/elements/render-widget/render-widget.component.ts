import {
  Component,
  OnInit,
  Input,
  ComponentFactoryResolver,
  ViewContainerRef,
} from "@angular/core";
import { WcmWidget } from "bpw-wcm-service";
import { appConfig } from "bpw-common";
// declare var appConfig: any;

@Component({
  // selector: "render-widget",
  templateUrl: "./render-widget.component.html",
  styleUrls: ["./render-widget.component.scss"],
})
export class RenderWidgetComponent implements OnInit {
  @Input() component: any;
  @Input() data: any;
  constructor(
    private _viewContainerRef: ViewContainerRef,
    private _componentFactoryResolver: ComponentFactoryResolver
  ) {}

  ngOnInit(): void {
    const componentFactory = this._componentFactoryResolver.resolveComponentFactory(
      appConfig.components[this.component]
    );
    this._viewContainerRef.clear();
    const componentRef = this._viewContainerRef.createComponent(
      componentFactory
    );
    (componentRef.instance as WcmWidget).data = this.data;
  }
}
