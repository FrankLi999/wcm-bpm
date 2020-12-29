import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { LoginModule } from "./login/login.module";
import { ForgotPasswordModule } from "./forgot-password/forgot-password.module";
import { ForgotPasswordComponent } from "./forgot-password/forgot-password/forgot-password.component";
import { LockScreenModule } from "./lock-screen/lock-screen.module";
import { LockScreenComponent } from "./lock-screen/lock-screen/lock-screen.component";
import { MailConfirmModule } from "./mail-confirm/mail-confirm.module";
import { MailConfirmComponent } from "./mail-confirm/mail-confirm/mail-confirm.component";
import { ResetPasswordModule } from "./reset-password/reset-password.module";
import { ResetPasswordComponent } from "./reset-password/reset-password/reset-password.component";
import { SignupModule } from "./signup/signup.module";
import { SignupComponent } from "./signup/signup/signup.component";
import { AuthComponent } from "./entry/auth/auth.component";
import { LoginComponent } from "./login/login/login.component";

import { VerificationModule } from "./verification/verification.module";

import { appConfig } from "bpw-common";
// declare var appConfig: any;
const routes: Routes = [
  {
    path: "auth",
    component: AuthComponent,
    children: [
      {
        path: "login",
        component: LoginComponent,
      },
      {
        path: "forgot-password",
        component: ForgotPasswordComponent,
      },
      {
        path: "reset-password",
        component: ResetPasswordComponent,
      },
      {
        path: "signup",
        component: SignupComponent,
      },
      {
        path: "lock-screen",
        component: LockScreenComponent,
      },
      {
        path: "mail-confirm/:id",
        component: MailConfirmComponent,
      },
      {
        path: "verification/:id",
        component: MailConfirmComponent,
      },
      {
        path: "**",
        redirectTo: appConfig.defaultUrl,
      },
    ],
  },
];
@NgModule({
  declarations: [AuthComponent],
  imports: [
    RouterModule.forChild(routes),
    LoginModule,
    ForgotPasswordModule,
    ResetPasswordModule,
    SignupModule,
    LockScreenModule,
    MailConfirmModule,
    VerificationModule,
  ],
  exports: [
    LoginModule,
    ForgotPasswordModule,
    ResetPasswordModule,
    SignupModule,
    LockScreenModule,
    MailConfirmModule,
  ],
})
export class AuthenticationModule {}
