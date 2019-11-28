import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from 'bpw-components';

@Component({
  selector: 'validation-rule-tree',
  templateUrl: './validation-rule-tree.component.html',
  styleUrls: ['./validation-rule-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations
})
export class ValidationRuleTreeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
