import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
@Component({
  selector: "authoring-template-dialog",
  templateUrl: "authoring-template-dialog.html",
  styleUrls: ["./authoring-template-dialog.scss"],
})
export class AuthoringTemplateDialog {
  constructor(
    public dialogRef: MatDialogRef<AuthoringTemplateDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
