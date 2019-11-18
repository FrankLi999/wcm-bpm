import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
@Component({
    selector: 'resource-type-dialog',
    templateUrl: 'resource-type-dialog.html',
  })
export class ResourceTypeDialog {
  
  constructor(
    public dialogRef: MatDialogRef<ResourceTypeDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
  