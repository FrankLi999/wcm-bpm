import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { FuseSharedModule } from 'bpw-components';
import { MailConfirmComponent } from './mail-confirm/mail-confirm.component';


const routes = [
  {
      path     : 'mail-confirm',
      component: MailConfirmComponent
  }
];

@NgModule({
  declarations: [MailConfirmComponent],
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule,
    FuseSharedModule,
    FlexLayoutModule
  ],
  exports: [MailConfirmComponent]
})
export class MailConfirmModule { }
