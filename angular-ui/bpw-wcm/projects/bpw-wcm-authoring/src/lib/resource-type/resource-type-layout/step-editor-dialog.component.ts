import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
@Component({
  selector: 'step-editor-dialog',
  templateUrl: 'step-editor-dialog.html',
})
export class StepEditorDialog {

  constructor(
    public dialogRef: MatDialogRef<StepEditorDialog>,
    @Inject(MAT_DIALOG_DATA) public data: string) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}