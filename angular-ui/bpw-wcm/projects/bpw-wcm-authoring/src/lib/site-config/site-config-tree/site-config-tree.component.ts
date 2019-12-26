import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { wcmAnimations } from 'bpw-common';
@Component({
  selector: 'site-config',
  templateUrl: './site-config-tree.component.html',
  styleUrls: ['./site-config-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : wcmAnimations
})
export class SiteConfigTreeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
