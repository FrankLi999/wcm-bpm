import { NgModule } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// import { FlexLayoutModule } from '@angular/flex-layout';
// import { RouterModule } from '@angular/router';
// import { MatButtonModule } from '@angular/material/button';
// import { MatCheckboxModule } from '@angular/material/checkbox';
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatIconModule } from '@angular/material/icon';
// import { MatInputModule } from '@angular/material/input';

// import { FuseSharedModule } from 'bpw-components';
// import { RestClientModule } from 'bpw-rest-client';
import { LoginModule } from './login/login.module';
import { ForgotPasswordModule } from './forgot-password/forgot-password.module';
import { LockScreenModule } from './lock-screen/lock-screen.module';
import { MailConfirmModule } from './mail-confirm/mail-confirm.module';
import { ResetPasswordModule } from './reset-password/reset-password.module';
import { SignupModule } from './signup/signup.module';
import { RestClientConfigModule } from 'bpw-rest-client';
import { FuseConfigModule } from 'bpw-components';
// import { authApiConfig, authLayoutConfig } from '../config';

// const routes = [
//   {
//       path     : 'login',
//       component: LoginComponent
//   },
//   {
//     path     : 'forgot-password',
//     component: ForgotPasswordComponent
//   },
//   {
//     path     : 'lock-screen',
//     component: LockScreenComponent
//   },
//   {
//     path     : 'mail-confirm',
//     component: MailConfirmComponent
//   },
//   {
//     path     : 'reset-password',
//     component: ResetPasswordComponent
//   },
//   {
//     path     : 'signup',
//     component: SignupComponent
//   }
// ];
@NgModule({
  imports: [
    LoginModule,
    ForgotPasswordModule,
    ResetPasswordModule,
    SignupModule,
    LockScreenModule,
    MailConfirmModule,
    // RestClientConfigModule.forChild(authApiConfig),
    // FuseConfigModule.forChild(authLayoutConfig)
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
