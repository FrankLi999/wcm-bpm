import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FlexLayoutModule } from "@angular/flex-layout";
import { RouterModule } from "@angular/router";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { ProfileComponent } from "./profile/profile.component";
import { SharedUIModule } from "bpw-common";

@NgModule({
  declarations: [ProfileComponent],
  imports: [
    CommonModule,
    FlexLayoutModule,
    SharedUIModule,
    RouterModule,
    PerfectScrollbarModule,
  ],
  exports: [ProfileComponent],
})
export class Oauth2ProfileModule {}
