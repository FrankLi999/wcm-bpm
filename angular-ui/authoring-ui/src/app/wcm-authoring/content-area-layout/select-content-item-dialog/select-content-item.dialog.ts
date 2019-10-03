import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { WcmFlatTreeNode } from '../../components/site-navigator/site-navigator.component';
@Component({
  selector: 'app-select-content-item-dialog',
  templateUrl: './select-content-item.dialog.html',
  styleUrls: ['./select-content-item.dialog.scss']
})
export class SelectContentItemDialog implements OnInit {
  selectedContentItems: string[];
  constructor(
    public dialogRef: MatDialogRef<SelectContentItemDialog>,
    @Inject(MAT_DIALOG_DATA) public data: {
      contentPath: string[],
      authoringTemplate: string
    }) {

      
  }

  ngOnInit() {
    this.selectedContentItems = [ ...this.data.contentPath];
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onOKClick() {
    this.dialogRef.close({
      selectedContentItems: this.selectedContentItems
    });
  }

  selectContent(activeNode: WcmFlatTreeNode) {
    return this.selectedContentItems.push(activeNode.wcmPath);
  }
}