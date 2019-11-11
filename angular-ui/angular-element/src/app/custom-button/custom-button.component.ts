import { Component, ViewEncapsulation, EventEmitter, Input, Output, ChangeDetectionStrategy, OnInit } from '@angular/core';

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
  encapsulation: ViewEncapsulation.Native,// .ShadowDom,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomButtonComponent implements OnInit {
  //@Input() label = 'default label';
  @Input() initial: number;
  @Output() action = new EventEmitter<number>();
  clicksCt =  0;

  ngOnInit() {
    this.clicksCt = this.initial ? this.initial : 1;
  }
  
  handleClick() {
    this.clicksCt++;
    this.action.emit(this.clicksCt);
  }
}