import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { RenderTemplateModel } from '../../model';
@Component({
    selector: 'select-render-template-dialog',
    templateUrl: 'select-render-template.dialog.html',
  })
  export class SelectRenderTemplateDialog {
  
    constructor(
      public dialogRef: MatDialogRef<SelectRenderTemplateDialog>,
      @Inject(MAT_DIALOG_DATA) public data: {
        renderTemplates: RenderTemplateModel[],
        selectedRenderTemplate: string}) {}
  
    onNoClick(): void {
      this.dialogRef.close();
    }
  }