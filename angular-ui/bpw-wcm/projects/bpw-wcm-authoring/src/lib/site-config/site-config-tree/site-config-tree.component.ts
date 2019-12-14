import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { fuseAnimations } from 'bpw-components';
@Component({
  selector: 'site-config',
  templateUrl: './site-config-tree.component.html',
  styleUrls: ['./site-config-tree.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations   : fuseAnimations
})
export class SiteConfigTreeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
