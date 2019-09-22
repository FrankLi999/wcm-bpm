import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from 'bpw-components';
@Component({
  selector: 'content-area-designer',
  templateUrl: './content-area-designer.component.html',
  styleUrls: ['./content-area-designer.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations
})
export class ContentAreaDesignerComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
