import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ResourceViewer, RenderTemplateModel } from 'bpw-wcm-service';
import { SelectContentItemDialog } from '../select-content-item-dialog/select-content-item.dialog';

@Component({
  selector: 'resource-viewer',
  templateUrl: './resource-viewer.component.html',
  styleUrls: ['./resource-viewer.component.scss']
})
export class ResourceViewerComponent implements OnInit {
  @Output() resourceViewerRemoved = new EventEmitter<number>();
  @Input() viewerIndex: number;
  @Input() resourceViewer: ResourceViewer;
  @Input() renderTemplate: RenderTemplateModel;
  constructor(private dialog: MatDialog) { }

  ngOnInit() {
  }
  
  public removeSideViewer(viewerIndex: number) {
    this.resourceViewerRemoved.emit(viewerIndex);
    return false;
  }

  public selectContentItems(viewerIndex: number) {
    const dialogRef = this.dialog.open(SelectContentItemDialog, {
      height: '80%',
      width: '60%',
      data: {
        authoringTemplate: this.renderTemplate.resourceName,
        contentPath: this.resourceViewer.contentPath
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data && data.selectedContentItems) {
        this.resourceViewer.contentPath = data.selectedContentItems;
      }
    });
    return false;
  }
}
