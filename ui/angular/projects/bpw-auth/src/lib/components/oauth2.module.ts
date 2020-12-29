import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { Oauth2ProfileModule } from "./oauth2-profile/oauth2-profile.module";
import { AuthGuard } from "bpw-common";
import { ProfileComponent } from "./oauth2-profile/profile/profile.component";

import { Oauth2RedirectHandlerModule } from "./oauth2-redirect-handler/oauth2-redirect-handler.module";
import { RedirectHandlerComponent } from "./oauth2-redirect-handler/redirect-handler/redirect-handler.component";
import { OAuth2Component } from "./entry/oauth2/oauth2.component";
import { appConfig } from "bpw-common";
const routes: Routes = [
  {
    path: "oauth2",
    component: OAuth2Component,
    children: [
      {
        path: "profile",
        component: ProfileComponent,
        canActivate: [AuthGuard],
      },
      {
        path: "redirect",
        component: RedirectHandlerComponent,
      },
      {
        path: "**",
        redirectTo: appConfig.defaultUrl,
      },
    ],
  },
];
@NgModule({
  declarations: [OAuth2Component],
  imports: [
    RouterModule.forChild(routes),
    Oauth2ProfileModule,
    Oauth2RedirectHandlerModule,
  ],
  exports: [Oauth2ProfileModule, Oauth2RedirectHandlerModule],
})
export class OAuth2Module {}
