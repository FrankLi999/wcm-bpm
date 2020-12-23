import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
@Component({
  selector: "tab-editor-dialog",
  templateUrl: "tab-editor-dialog.html",
})
export class WcmTabEditorDialog {
  constructor(
    public dialogRef: MatDialogRef<WcmTabEditorDialog>,
    @Inject(MAT_DIALOG_DATA) public data: string
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
