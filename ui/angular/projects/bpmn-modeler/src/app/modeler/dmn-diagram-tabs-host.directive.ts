import { Directive, ViewContainerRef } from "@angular/core";

@Directive({
  selector: "[dmnDiagramTabsHost]",
})
export class DmnDiagramTabsHostDirective {
  constructor(public viewContainerRef: ViewContainerRef) {}
}
