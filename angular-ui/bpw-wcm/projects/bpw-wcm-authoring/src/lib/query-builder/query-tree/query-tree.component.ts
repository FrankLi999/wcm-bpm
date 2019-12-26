import { Component, OnInit,ViewEncapsulation } from '@angular/core';
import { wcmAnimations } from 'bpw-common';

@Component({
  selector: 'query-tree',
  templateUrl: './query-tree.component.html',
  styleUrls: ['./query-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : wcmAnimations
})
export class QueryTreeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
