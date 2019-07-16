import { NgModule } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { FuseSharedModule } from '@fuse/shared.module';
import { LoginModule } from './login/login.module';
import { ForgotPasswordModule } from './forgot-password/forgot-password.module';
import { ResetPasswordModule } from './reset-password/reset-password.module';
import { SignupModule } from './signup/signup.module';
import { LockScreenModule } from './lock-screen/lock-screen.module';
import { MailConfirmModule } from './mail-confirm/mail-confirm.module';

@NgModule({
  declarations: [
  ],
  imports: [
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
