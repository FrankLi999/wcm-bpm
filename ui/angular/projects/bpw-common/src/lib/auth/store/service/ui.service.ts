import { Injectable, Component, Inject } from "@angular/core";
import { Router } from "@angular/router";
import { MatSnackBar, MatSnackBarConfig } from "@angular/material/snack-bar";
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialogConfig,
} from "@angular/material/dialog";
import { Observable } from "rxjs";

//TDOD: handle validation error
@Injectable({
  providedIn: "root",
})
export class UiService {
  constructor(
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private router: Router
  ) {}

  getErrorMessage(err): string {
    // return err.error && err.error.errors.length > 0
    return err.error && err.error.errors && err.error.errors.length > 0
      ? "validation error"
      : err.error && err.error.message
      ? err.error.message
      : err.message;
  }

  gotoApplicationErrorPage(errorMessage) {
    this.router.navigate(["/app-error"], { queryParams: { errorMessage } });
  }

  showToast(message: string, action = "Close", config?: MatSnackBarConfig) {
    this.snackBar.open(
      message,
      action,
      config || {
        duration: 7000,
      }
    );
  }

  showDialog(
    title: string,
    content: string,
    okText = "OK",
    cancelText?: string,
    customConfig?: MatDialogConfig
  ): Observable<Boolean> {
    const dialogRef = this.dialog.open(
      SimpleDialogComponent,
      customConfig || {
        width: "300px",
        data: {
          title: title,
          content: content,
          okText: okText,
          cancelText: cancelText,
        },
      }
    );

    return dialogRef.afterClosed();
  }

  getUserProfile() {
    let userProfile = null;
    if (sessionStorage.getItem("bpw_accessToken")) {
      userProfile = {
        id: sessionStorage.getItem("bpw_uid"),
        email: sessionStorage.getItem("bpw_email"),
        firstName: sessionStorage.getItem("bpw_firstName"),
        lastName: sessionStorage.getItem("bpw_lastName"),
        name: sessionStorage.getItem("bpw_name"),
        imageUrl: sessionStorage.getItem("bpw_imageUrl"),
        accessToken: sessionStorage.getItem("bpw_accessToken"),
        roles: sessionStorage.getItem("bpw_roles").split(","),
        tokenType: sessionStorage.getItem("bpw_tokenType"),
        expireIn: parseInt(sessionStorage.getItem("bpw_expireIn")),
      };
    }
    return userProfile;
  }

  saveUserProfile(userProfile) {
    sessionStorage.setItem("bpw_uid", userProfile.id);
    sessionStorage.setItem("bpw_email", userProfile.email);
    if (userProfile.firstName) {
      sessionStorage.setItem("bpw_firstName", userProfile.firstName);
    }
    if (userProfile.lastName) {
      sessionStorage.setItem("bpw_lastName", userProfile.lastName);
    }
    if (userProfile.name) {
      sessionStorage.setItem("bpw_name", userProfile.name);
    }
    if (userProfile.imageUrl) {
      sessionStorage.setItem("bpw_imageUrl", userProfile.imageUrl);
    }
    sessionStorage.setItem("bpw_accessToken", userProfile.accessToken);
    sessionStorage.setItem("bpw_expireIn", `${userProfile.expireIn}`);
    sessionStorage.setItem(
      "bpw_roles",
      userProfile.roles ? userProfile.roles.join(",") : ""
    );
    sessionStorage.setItem("bpw_tokenType", userProfile.tokenType);
  }
  isLoggedIn(): boolean {
    return sessionStorage.removeItem("bpw_accessToken") != null;
  }
  clearUserProfile() {
    sessionStorage.removeItem("bpw_uid");
    sessionStorage.removeItem("bpw_email");
    sessionStorage.removeItem("bpw_firstName");
    sessionStorage.removeItem("bpw_lastName");
    sessionStorage.removeItem("bpw_name");
    sessionStorage.removeItem("bpw_imageUrl");
    sessionStorage.removeItem("bpw_accessToken");
    sessionStorage.removeItem("bpw_roles");
    sessionStorage.removeItem("bpw_expireIn");
    sessionStorage.removeItem("bpw_tokenType");
  }
}

@Component({
  selector: "app-simple-dialog",
  template: `
    <h2 mat-dialog-title>data.title</h2>
    <mat-dialog-content>
      <p>data.content</p>
    </mat-dialog-content>
    <mat-dialog-actions>
      <span class="flex-spacer"></span>
      <button mat-button mat-dialog-close *ngIf="data.cancelText">
        data.cancelText
      </button>
      <button
        mat-button
        mat-button-raised
        color="primary"
        [mat-dialog-close]="true"
        cdkFocusInitial
      >
        data.okText
      </button>
    </mat-dialog-actions>
  `,
})
export class SimpleDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<SimpleDialogComponent, Boolean>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}
}
