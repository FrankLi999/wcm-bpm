import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'wrapper',
  template: `
  <div class="box red">
    <ng-content></ng-content>
  </div>
  <div class="box">
    <ng-content select="counter"></ng-content>
  </div>
  <ng-container ngProjectAs="counter">
    <counter></counter>
  </ng-container>
  <div class="box">
    <ng-content></ng-content>
  </div>
  <div class="box">
    <ng-content></ng-content>
  </div>
  `
})
export class WrapperComponent {}