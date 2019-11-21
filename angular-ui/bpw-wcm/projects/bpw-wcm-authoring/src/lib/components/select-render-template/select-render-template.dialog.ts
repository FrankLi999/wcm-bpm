import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { RenderTemplateModel } from 'bpw-wcm-service';
@Component({
    selector: 'select-render-template-dialog',
    templateUrl: 'select-render-template.dialog.html',
  })
  export class SelectRenderTemplateDialog {
  
    constructor(
      public dialogRef: MatDialogRef<SelectRenderTemplateDialog>,
      @Inject(MAT_DIALOG_DATA) public data: any
      // {
      //   renderTemplates: RenderTemplateModel[],
      //   selectedRenderTemplate: string
      // }
      ) {}
  
    onNoClick(): void {
      this.dialogRef.close();
    }
  }