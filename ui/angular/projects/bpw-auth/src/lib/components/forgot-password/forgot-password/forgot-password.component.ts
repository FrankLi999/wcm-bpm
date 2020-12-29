import {
  Component,
  OnInit,
  ViewEncapsulation,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

import {
  AuthService,
  authLayoutConfig,
  BlockUIService,
  UIConfigService,
  wcmAnimations,
} from "bpw-common";

@Component({
  selector: "forgot-password",
  templateUrl: "./forgot-password.component.html",
  styleUrls: ["./forgot-password.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ForgotPasswordComponent implements OnInit {
  forgotPasswordForm: FormGroup;
  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  /**
   * Constructor
   *
   * @ param {UIConfigService} _uiConfigService
   * @ param {FormBuilder} _formBuilder
   */
  constructor(
    private _blockUIService: BlockUIService,
    private _authService: AuthService,
    private _uiConfigService: UIConfigService,
    private _formBuilder: FormBuilder
  ) {
    // Configure the layout
    this._uiConfigService.config = {
      ...authLayoutConfig,
    };
  }

  // -----------------------------------------------------------------------------------------------------
  // @ Lifecycle hooks
  // -----------------------------------------------------------------------------------------------------

  /**
   * On init
   */
  ngOnInit(): void {
    this.forgotPasswordForm = this._formBuilder.group({
      email: ["", [Validators.required, Validators.email]],
    });
  }

  sendResetLink() {
    this.componentRef = this._createBlockUIComponent("Send reset link");
    this._authService
      .forgotPassword(this.forgotPasswordForm.value.email)
      .subscribe(() => {
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
