import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { FuseSharedModule } from 'bpw-components';
import { LoginModule } from './login/login.module';
import { ForgotPasswordModule } from './forgot-password/forgot-password.module';
import { ResetPasswordModule } from './reset-password/reset-password.module';
import { SignupModule } from './signup/signup.module';
import { LockScreenModule } from './lock-screen/lock-screen.module';
import { MailConfirmModule } from './mail-confirm/mail-confirm.module';
import { AuthStoreModule } from '../store/auth-store.module';
@NgModule({
  declarations: [
  ],
  imports: [
      LoginModule,
      HttpClientModule,
      ForgotPasswordModule,
      ResetPasswordModule,
      SignupModule,
      LockScreenModule,
      MailConfirmModule,
      TranslateModule,
      FuseSharedModule,
      AuthStoreModule
  ],
  exports: [
    LoginModule,
    ForgotPasswordModule,
    ResetPasswordModule,
    SignupModule,
    LockScreenModule,
    MailConfirmModule,
    TranslateModule,
    FuseSharedModule
  ]
})
export class AuthenticationModule { }
