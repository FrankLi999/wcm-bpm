import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

import { FuseSharedModule } from 'bpw-components';

import { LoginComponent } from './login/login/login.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password/forgot-password.component';
import { LockScreenComponent } from './lock-screen/lock-screen/lock-screen.component';
import { MailConfirmComponent } from './mail-confirm/mail-confirm/mail-confirm.component';
import { ResetPasswordComponent } from './reset-password/reset-password/reset-password.component';
import { SignupComponent } from './signup/signup/signup.component';

const routes = [
  {
      path     : 'login',
      component: LoginComponent
  },
  {
    path     : 'forgot-password',
    component: ForgotPasswordComponent
  },
  {
    path     : 'lock-screen',
    component: LockScreenComponent
  },
  {
    path     : 'mail-confirm',
    component: MailConfirmComponent
  },
  {
    path     : 'reset-password',
    component: ResetPasswordComponent
  },
  {
    path     : 'signup',
    component: SignupComponent
  }
];
@NgModule({
  declarations: [
    LoginComponent,
    ForgotPasswordComponent,
    LockScreenComponent,
    MailConfirmComponent,
    ResetPasswordComponent,
    SignupComponent
  ],
  imports: [
      RouterModule.forChild(routes),
      CommonModule,
      FormsModule, 
      ReactiveFormsModule,
      MatButtonModule,
      MatCheckboxModule,
      MatFormFieldModule,
      MatIconModule,
      MatInputModule,
      FuseSharedModule,
      FlexLayoutModule,
      FuseSharedModule
  ],
  exports: [
    LoginComponent,
    ForgotPasswordComponent,
    LockScreenComponent,
    MailConfirmComponent,
    ResetPasswordComponent,
    SignupComponent
  ]
})
export class AuthenticationModule { }
