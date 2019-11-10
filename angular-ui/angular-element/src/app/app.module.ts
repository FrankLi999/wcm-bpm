import { BrowserModule } from '@angular/platform-browser';
import { NgModule, Injector, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { createCustomElement } from '@angular/elements';
import { ContainerComponent } from './custom-button/container.component';
import { CustomButtonComponent } from './custom-button/custom-button.component';
@NgModule({
  declarations: [
    ContainerComponent,
    CustomButtonComponent
  ],
  imports: [
    BrowserModule
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  // bootstrap: [AppComponent],
  entryComponents: [
    ContainerComponent,
    CustomButtonComponent
  ]
})
export class AppModule { 
  constructor(private injector: Injector) {
  }

  ngDoBootstrap() {
      const customButtonComponent = createCustomElement(CustomButtonComponent, { injector: this.injector });
      customElements.define('custom-button', customButtonComponent);

      const ContainerComponent = createCustomElement(CustomButtonComponent, { injector: this.injector });
      customElements.define('Container-component', ContainerComponent);
  }
}