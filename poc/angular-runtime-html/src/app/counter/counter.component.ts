import { Component, OnInit } from '@angular/core';

let instances = 0;

@Component({
  selector: 'counter',
  template: '<h1>{{this.id}}</h1>'
})
export class CounterComponent {
  id: number;
  
  constructor() {
    this.id = ++instances;
  }
}