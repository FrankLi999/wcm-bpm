import { Injectable } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";

import { ConfirmDialogComponent } from "./confirm-dialog.component";

@Injectable({
  providedIn: "root"
})
export class ConfirmationDialogService {
  confirmDialogRef: MatDialogRef<ConfirmDialogComponent>;
  constructor(private _matDialog: MatDialog) {}
  public confirm(message: string): MatDialogRef<ConfirmDialogComponent> {
    this.confirmDialogRef = this._matDialog.open(ConfirmDialogComponent, {
      disableClose: false
    });

    this.confirmDialogRef.componentInstance.confirmMessage = message;
    return this.confirmDialogRef;
  }

  closeConfirmation() {
    this.confirmDialogRef = null;
  }
}
