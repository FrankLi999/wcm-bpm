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

import { SharedUIModule } from '../../../common/shared-ui.module';
import { RestClientModule } from 'bpw-rest-client';
import { LoginComponent } from './login/login.component';
import { AuthStoreModule } from '../../store/auth-store.module';

const routes = [
  {
      path     : 'login',
      component: LoginComponent
  }
];

@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    RestClientModule,
    RouterModule.forChild(routes),
    CommonModule,
    FormsModule, 
    ReactiveFormsModule,
    MatButtonModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    SharedUIModule,
    FlexLayoutModule,
    AuthStoreModule
  ],
  exports: [
    LoginComponent
  ]
})
export class LoginModule { }
