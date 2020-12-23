import { NgModule, CUSTOM_ELEMENTS_SCHEMA, Injector } from "@angular/core";
import { RouterModule } from "@angular/router";
import { createCustomElement } from "@angular/elements";
import { RenderElementComponent } from "./elements/render-element/render-element.component";
import { RenderPropertyComponent } from "./elements/render-property/render-property.component";
import { QueryResultColumnComponent } from "./elements/query-result-column/query-result-column.component";
import { LinkElementComponent } from "./elements/link-element/link-element.component";
import { RenderFragementComponent } from "./elements/render-fragement/render-fragement.component";
import { RenderWidgetComponent } from "./elements/render-widget/render-widget.component";

@NgModule({
  declarations: [
    RenderElementComponent,
    RenderPropertyComponent,
    QueryResultColumnComponent,
    LinkElementComponent,
    RenderFragementComponent,
    RenderWidgetComponent,
  ],
  imports: [RouterModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [
    RenderElementComponent,
    RenderPropertyComponent,
    QueryResultColumnComponent,
    LinkElementComponent,
    RenderFragementComponent,
    RenderWidgetComponent,
  ],
})
export class WcmElementsModule {
  constructor(private injector: Injector) {
    const renderElementElementComponent = createCustomElement(
      RenderElementComponent,
      { injector: injector }
    );
    const renderPropertyElementComponent = createCustomElement(
      RenderPropertyComponent,
      { injector: injector }
    );
    const queryResultColumnComponent = createCustomElement(
      QueryResultColumnComponent,
      { injector: injector }
    );

    const renderFragementComponent = createCustomElement(
      RenderFragementComponent,
      {
        injector: injector,
      }
    );

    const renderWidgetComponent = createCustomElement(RenderWidgetComponent, {
      injector: injector,
    });

    const linkElementComponent = createCustomElement(LinkElementComponent, {
      injector: injector,
    });

    customElements.define("render-element", renderElementElementComponent);
    customElements.define("render-property", renderPropertyElementComponent);
    customElements.define("query-result-column", queryResultColumnComponent);
    customElements.define("link-element", linkElementComponent);
    customElements.define("render-fragement", renderFragementComponent);
    customElements.define("render-widget", renderWidgetComponent);
  }
}
