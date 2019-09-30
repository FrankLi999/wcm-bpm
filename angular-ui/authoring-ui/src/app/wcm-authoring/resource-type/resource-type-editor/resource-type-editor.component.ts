import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'resource-type-editor',
  templateUrl: './resource-type-editor.component.html',
  styleUrls: ['./resource-type-editor.component.scss']
})
export class ResourceTypeEditorComponent implements OnInit {

  @Input() resourceTypeName: string;
  constructor() { }

  ngOnInit() {
  }

}
