import {
  Component,
  OnDestroy,
  OnInit,
  ViewEncapsulation,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/internal/operators";

import {
  AuthService,
  authLayoutConfig,
  BlockUIService,
  UIConfigService,
  wcmAnimations,
  ResetPassword,
} from "bpw-common";

@Component({
  selector: "reset-password",
  templateUrl: "./reset-password.component.html",
  styleUrls: ["./reset-password.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ResetPasswordComponent implements OnInit, OnDestroy {
  resetPasswordForm: FormGroup;
  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  // Private
  private _unsubscribeAll: Subject<any>;

  constructor(
    private _blockUIService: BlockUIService,
    private _authService: AuthService,
    private _uiConfigService: UIConfigService,
    private _formBuilder: FormBuilder,
    private _route: ActivatedRoute
  ) {
    // Configure the layout
    this._uiConfigService.config = {
      ...authLayoutConfig,
    };

    // Set the private defaults
    this._unsubscribeAll = new Subject();
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Lifecycle hooks
  // -----------------------------------------------------------------------------------------------------

  /**
   * On init
   */
  ngOnInit(): void {
    this.resetPasswordForm = this._formBuilder.group({
      // name: ["", Validators.required],
      // email: ["", [Validators.required, Validators.email]],
      password: ["", Validators.required],
      passwordConfirm: ["", [Validators.required, confirmPasswordValidator]],
    });

    // Update the validity of the 'passwordConfirm' field
    // when the 'password' field changes
    this.resetPasswordForm
      .get("password")
      .valueChanges.pipe(takeUntil(this._unsubscribeAll))
      .subscribe(() => {
        this.resetPasswordForm.get("passwordConfirm").updateValueAndValidity();
      });
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    // Unsubscribe from all subscriptions
    this._unsubscribeAll.next();
    this._unsubscribeAll.complete();
  }

  resetPassword() {
    let resetPassword: ResetPassword = {
      code: this._route.snapshot.queryParams["code"],
      newPassword: this.resetPasswordForm.value.password,
    };
    this.componentRef = this._createBlockUIComponent("Reset password");
    this._authService.resetPassword(resetPassword).subscribe(() => {
      this._destroyBlockUIComponent();
    });
  }

  private _createBlockUIComponent(message: string) {
    this.componentRef = this._blockUIService.createBlockUIComponent(
      message,
      this.blockui
    );
    this.blocking = true;
  }

  private _destroyBlockUIComponent() {
    this._blockUIService.destroyBlockUIComponent(
      this.blockui,
      this.componentRef
    );
    this.blocking = false;
  }
}

/**
 * Confirm password validator
 *
 * @ param {AbstractControl} control
 * @ returns {ValidationErrors | null}
 */
const confirmPasswordValidator: ValidatorFn = (
  control: AbstractControl
): ValidationErrors | null => {
  if (!control.parent || !control) {
    return null;
  }

  const password = control.parent.get("password");
  const passwordConfirm = control.parent.get("passwordConfirm");

  if (!password || !passwordConfirm) {
    return null;
  }

  if (passwordConfirm.value === "") {
    return null;
  }

  if (password.value === passwordConfirm.value) {
    return null;
  }

  return { passwordsNotMatching: true };
};
