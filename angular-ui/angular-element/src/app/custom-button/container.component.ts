import { Input, Component, ViewEncapsulation, EventEmitter, Output } from '@angular/core';

@Component({
  template: `<p><custom-button (clicksct)="onClicksCt()">Test</custom-button></p>`,
  encapsulation: ViewEncapsulation.Native
})
export class ContainerComponent {

  onClicksCt() {
    console.log("Clicks count!!!");
  }
}
