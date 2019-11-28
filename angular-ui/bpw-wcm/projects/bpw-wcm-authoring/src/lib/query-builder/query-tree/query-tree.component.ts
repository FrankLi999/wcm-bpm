import { Component, OnInit,ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from 'bpw-components';

@Component({
  selector: 'query-tree',
  templateUrl: './query-tree.component.html',
  styleUrls: ['./query-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations
})
export class QueryTreeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
