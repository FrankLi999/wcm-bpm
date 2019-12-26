import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { SharedUIModule } from '../../../common/shared-ui.module';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';

const routes = [
  {
      path     : 'forgot-password',
      component: ForgotPasswordComponent
  }
];
@NgModule({
  declarations: [ForgotPasswordComponent],
  imports: [
      CommonModule,
      FormsModule,
      ReactiveFormsModule,
      FlexLayoutModule,
      RouterModule.forChild(routes),
      CommonModule,
      FormsModule, 
      ReactiveFormsModule,
      MatButtonModule,
      MatFormFieldModule,
      MatIconModule,
      MatInputModule,

      SharedUIModule,
      FlexLayoutModule
  ],
  exports: [ForgotPasswordComponent]
})
export class ForgotPasswordModule { }
