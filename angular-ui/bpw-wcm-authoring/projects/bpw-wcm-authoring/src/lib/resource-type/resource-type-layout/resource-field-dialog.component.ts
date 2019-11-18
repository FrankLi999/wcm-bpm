import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
@Component({
    selector: 'resource-field-dialog',
    templateUrl: 'resource-field-dialog.html',
  })
  export class ResourceFieldDialog {
  
    constructor(
      public dialogRef: MatDialogRef<ResourceFieldDialog>,
      @Inject(MAT_DIALOG_DATA) public data: any
      // {
      //   templateField:TemplateField,
      //   readonly controlField: ControlField
      // }
      ) {}
  
    onNoClick(): void {
      this.dialogRef.close();
    }
  }