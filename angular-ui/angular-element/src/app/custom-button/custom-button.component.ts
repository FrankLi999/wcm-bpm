import { Component, ViewEncapsulation, EventEmitter, Output, ChangeDetectionStrategy } from '@angular/core';

@Component({
  template: `<button (click)="handleClick()"><slot></slot>:{{clicksCt}}</button>`,
  styles: [`
    button {
      border: solid 3px;
      padding: 8px 10px;
      background: #bada55;
      font-size: 20px;
    }
  `],
  encapsulation: ViewEncapsulation.ShadowDom,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomButtonComponent {
  //@Input() label = 'default label';
  @Output() action = new EventEmitter<number>();
  clicksCt = 0;

  handleClick() {
    this.clicksCt++;
    this.action.emit(this.clicksCt);
  }
}