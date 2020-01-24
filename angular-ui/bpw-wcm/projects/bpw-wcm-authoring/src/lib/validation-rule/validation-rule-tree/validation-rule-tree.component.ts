import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { wcmAnimations } from 'bpw-common';

@Component({
  selector: 'validation-rule-tree',
  templateUrl: './validation-rule-tree.component.html',
  styleUrls: ['./validation-rule-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : wcmAnimations
})
export class ValidationRuleTreeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
