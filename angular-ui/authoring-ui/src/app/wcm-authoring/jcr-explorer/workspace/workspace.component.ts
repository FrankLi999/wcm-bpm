import { Component, OnInit, Input, ViewEncapsulation } from '@angular/core';
import { Workspace } from '../../model';

@Component({
  selector: 'workspace',
  templateUrl: './workspace.component.html',
  styleUrls: ['./workspace.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class WorkspaceComponent implements OnInit {

  @Input() workspace: Workspace;
  constructor() { }

  ngOnInit() {
  }

}
