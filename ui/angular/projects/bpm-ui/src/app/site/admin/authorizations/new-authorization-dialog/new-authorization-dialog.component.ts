import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
@Component({
  selector: "new-authorization-dialog",
  templateUrl: "./new-authorization-dialog.component.html",
  styleUrls: ["./new-authorization-dialog.component.scss"],
})
export class NewAuthorizationDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<NewAuthorizationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  isGroup(): boolean {
    return this.data.authorization.groupId != null;
  }

  isUser(): boolean {
    return this.data.authorization.userId != null;
  }

  switchToGroup() {
    this.data.authorization.groupId = this.data.authorization.userId || "";
    this.data.authorization.userId = null;
  }

  switchToUser() {
    this.data.authorization.userId = this.data.authorization.groupId || "";
    this.data.authorization.groupId = null;
  }

  getPermissions() {
    return this.data.authorization.permissions
      ? this.data.authorization.permissions.includes("ALL")
        ? "ALL"
        : this.data.authorization.permissions.join(", ")
      : "";
  }

  toggleAllSelection() {
    if (this.data.authorization.permissions.includes("ALL")) {
      this.data.authorization.permissions = [...this.data.resource.permissions];
      this.data.authorization.permissions.unshift("ALL");
    } else {
      this.data.authorization.permissions = [];
    }
  }

  togglePerOne() {
    if (this.data.authorization.permissions.includes("ALL")) {
      this.data.authorization.permissions.shift();
      this.data.authorization.permissions = [
        ...this.data.authorization.permissions,
      ];
    }
    if (
      this.data.authorization.permissions.length ===
      this.data.resource.permissions.length
    ) {
      this.data.authorization.permissions = [
        "ALL",
        ...this.data.authorization.permissions.permissions,
      ];
    }
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
