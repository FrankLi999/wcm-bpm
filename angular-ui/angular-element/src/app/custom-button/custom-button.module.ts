import { NgModule, Injector, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { createCustomElement } from '@angular/elements';

import { CustomButtonComponent } from './custom-button.component';
import { ContainerComponent } from './container.component';
@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    CustomButtonComponent,
    ContainerComponent
  ],
  entryComponents: [
    CustomButtonComponent,
    ContainerComponent
  ],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ]
})
export class CustomButtonModule {
  constructor(private injector: Injector) {
    const madeWithLoveElement = createCustomElement(CustomButtonComponent, { injector });
    customElements.define('custom-button', madeWithLoveElement);
    const customContainer = createCustomElement(ContainerComponent, { injector: injector });
    customElements.define('custom-container', customContainer);
  }
}