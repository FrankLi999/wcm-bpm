import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { JsonForm } from "bpw-wcm-service";
import { FormConfig } from "../../config/form-config";

@Component({
  selector: "resource-field-dialog",
  templateUrl: "resource-field-dialog.html",
})
export class WcmResourceFieldDialog {
  controlFieldForm: JsonForm;
  formConfig = FormConfig;
  formControlData: any;
  constructor(
    public dialogRef: MatDialogRef<WcmResourceFieldDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.controlFieldForm = this.data.controlFieldForm;
    this.formControlData = this.data.controlField;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  saveUpdate(formData) {
    this.dialogRef.close(formData);
  }
}
