import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { FuseSharedModule } from '@fuse/shared.module';
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
    MatIconModule,
    FuseSharedModule
  ]
})
export class MailConfirmModule { }
