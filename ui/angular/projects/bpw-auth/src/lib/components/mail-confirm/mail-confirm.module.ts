import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FlexLayoutModule } from "@angular/flex-layout";
import { RouterModule } from "@angular/router";
import { MatIconModule } from "@angular/material/icon";

import { SharedUIModule } from "bpw-common";
import { MailConfirmComponent } from "./mail-confirm/mail-confirm.component";

@NgModule({
  declarations: [MailConfirmComponent],
  imports: [
    RouterModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule,
    SharedUIModule,
    FlexLayoutModule,
  ],
  exports: [MailConfirmComponent],
})
export class MailConfirmModule {}
