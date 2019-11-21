import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { WcmItemFlatTreeNode } from '../wcm-navigator/wcm-navigator.component';
@Component({
  selector: 'select-content-item-dialog',
  templateUrl: './select-content-item.dialog.html',
  styleUrls: ['./select-content-item.dialog.scss']
})
export class SelectContentItemDialog implements OnInit {
  selectedContentItems: string[];
  constructor(
    public dialogRef: MatDialogRef<SelectContentItemDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any
    // {
    //   contentPath: string[],
    //   authoringTemplate: string
    // }
    ) {

      
  }

  ngOnInit() {
    this.selectedContentItems = this.data.contentPath ? [ ...this.data.contentPath] : [];
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onOKClick() {
    this.dialogRef.close({
      selectedContentItems: this.selectedContentItems
    });
  }

  selectContent(activeNode: WcmItemFlatTreeNode) {
    return this.selectedContentItems.push(activeNode.wcmPath);
  }
}