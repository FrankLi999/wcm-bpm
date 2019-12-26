import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { wcmAnimations } from 'bpw-common';
@Component({
  selector: 'workflow-tree',
  templateUrl: './workflow-tree.component.html',
  styleUrls: ['./workflow-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : wcmAnimations
})
export class WorkflowTreeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
