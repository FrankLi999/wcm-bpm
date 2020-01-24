import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { wcmAnimations } from 'bpw-common';

@Component({
  selector: 'category-tree',
  templateUrl: './category-tree.component.html',
  styleUrls: ['./category-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : wcmAnimations
})
export class CategoryTreeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
