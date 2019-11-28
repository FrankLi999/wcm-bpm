import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from 'bpw-components';
@Component({
  selector: 'workflow-tree',
  templateUrl: './workflow-tree.component.html',
  styleUrls: ['./workflow-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations
})
export class WorkflowTreeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
