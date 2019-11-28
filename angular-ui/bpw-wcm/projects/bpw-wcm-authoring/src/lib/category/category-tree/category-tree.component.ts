import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from 'bpw-components';

@Component({
  selector: 'category-tree',
  templateUrl: './category-tree.component.html',
  styleUrls: ['./category-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations
})
export class CategoryTreeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
