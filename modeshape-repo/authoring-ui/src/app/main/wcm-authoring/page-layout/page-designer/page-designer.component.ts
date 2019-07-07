import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from '@fuse/animations';
@Component({
  selector: 'page-designer',
  templateUrl: './page-designer.component.html',
  styleUrls: ['./page-designer.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations
})
export class PageDesignerComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
