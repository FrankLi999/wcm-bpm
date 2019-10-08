import { Component, OnInit } from '@angular/core';
import { ContentAreaLayoutComponent } from '../content-area-layout/content-area-layout.component';
@Component({
  selector: 'new-content-area-layout',
  templateUrl: './new-content-area-layout.component.html',
  styleUrls: ['./new-content-area-layout.component.scss']
})
export class NewContentAreaLayoutComponent extends ContentAreaLayoutComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
