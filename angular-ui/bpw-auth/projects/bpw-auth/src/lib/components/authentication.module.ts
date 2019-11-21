import { NgModule } from '@angular/core';
import { LoginModule } from './login/login.module';
import { ForgotPasswordModule } from './forgot-password/forgot-password.module';
import { LockScreenModule } from './lock-screen/lock-screen.module';
import { MailConfirmModule } from './mail-confirm/mail-confirm.module';
import { ResetPasswordModule } from './reset-password/reset-password.module';
import { SignupModule } from './signup/signup.module';

@NgModule({
  imports: [
    LoginModule,
    ForgotPasswordModule,
    ResetPasswordModule,
    SignupModule,
    LockScreenModule,
    MailConfirmModule,
  ],
  exports: [
    LoginModule,
    ForgotPasswordModule,
    ResetPasswordModule,
    SignupModule,
    LockScreenModule,
    MailConfirmModule
  ]
})
export class AuthenticationModule { }
