import { Component, OnInit } from "@angular/core";
import { AuthService, UserProfile } from "bpw-common";
@Component({
  // selector: "user-profile",
  templateUrl: "./user-profile.component.html",
  styleUrls: ["./user-profile.component.scss"],
})
export class UserProfileComponent implements OnInit {
  constructor(private _authService: AuthService) {}
  userProfile: UserProfile;
  ngOnInit(): void {
    this._authService
      .getUserProfile()
      .subscribe((userProfile) => (this.userProfile = userProfile));
  }
}
