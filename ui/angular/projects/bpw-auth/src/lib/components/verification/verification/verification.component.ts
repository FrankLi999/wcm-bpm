import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { AuthService } from "bpw-common";
@Component({
  selector: "auth-verification",
  templateUrl: "./verification.component.html",
  styleUrls: ["./verification.component.scss"],
})
export class VerificationComponent implements OnInit {
  constructor(
    private _router: Router,
    private _route: ActivatedRoute,
    private _authService: AuthService
  ) {}

  ngOnInit(): void {
    const code = this._route.snapshot.queryParams["code"];
    const id = this._route.snapshot.queryParams["id"];
    this._authService.verification(id, code).subscribe(() => {
      this._router.navigate(["/auth/login"]);
    });
  }
}
