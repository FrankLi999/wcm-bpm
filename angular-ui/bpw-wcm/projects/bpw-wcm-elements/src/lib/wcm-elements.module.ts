import { NgModule, CUSTOM_ELEMENTS_SCHEMA, Injector } from '@angular/core'

import { createCustomElement } from '@angular/elements';
import { RenderElementComponent } from './elements/render-element/render-element.component';



@NgModule({
  declarations: [RenderElementComponent],
  imports: [
  ],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  exports: [RenderElementComponent],
  entryComponents: [
    RenderElementComponent
  ]
})
export class WcmElementsModule { 
  constructor(private injector: Injector) {  
    const renderElementElementComponent = createCustomElement(RenderElementComponent, { injector: injector });
    customElements.define('render-element', renderElementElementComponent);
}
}
