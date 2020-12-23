import { Component, OnInit, ViewChild, Input, ViewEncapsulation } from '@angular/core';
import { RestNode } from 'bpw-wcm-service';

@Component({
  selector: 'jcr-node',
  templateUrl: './jcr-node.component.html',
  styleUrls: ['./jcr-node.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class JcrNodeComponent implements OnInit {
  @Input() restNode: RestNode;
  constructor() {
  }

  ngOnInit() {
  }
}