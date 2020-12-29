import { Component, ViewEncapsulation, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { AuthService, wcmAnimations } from "bpw-common";

@Component({
  selector: "mail-confirm",
  templateUrl: "./mail-confirm.component.html",
  styleUrls: ["./mail-confirm.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class MailConfirmComponent implements OnInit {
  /**
   * Constructor
   *
   * @ param {UIConfigService} _wcmConfigService
   */
  constructor(
    private _authService: AuthService,
    private _route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this._route.snapshot.queryParams["id"];
    this._authService.resendVerification(id).subscribe();
  }
}
