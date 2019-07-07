import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Viewer } from '../page-layout/page-layout.component';
@Component({
  selector: 'resource-viewer',
  templateUrl: './resource-viewer.component.html',
  styleUrls: ['./resource-viewer.component.scss']
})
export class ResourceViewerComponent implements OnInit {
  @Output() resourceViewerRemoved = new EventEmitter<number>();
  @Input() viewer: Viewer;
  @Input() viewerIndex: number;
  constructor() { }

  ngOnInit() {
  }

  public addRenderTemplate() {
    this.viewer.renderTemplates.push("a_render_tmplate");
  }
  
  public removeSideViewer(viewerIndex: number) {
    this.resourceViewerRemoved.emit(viewerIndex);
    return false;
  }
}
