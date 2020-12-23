import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FlexLayoutModule } from "@angular/flex-layout";
import { RouterModule } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { SharedUIModule } from "bpw-common";
import { ResetPasswordComponent } from "./reset-password/reset-password.component";
@NgModule({
  declarations: [ResetPasswordComponent],
  imports: [
    RouterModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    PerfectScrollbarModule,
    SharedUIModule,
    FlexLayoutModule,
  ],
  exports: [ResetPasswordComponent],
})
export class ResetPasswordModule {}
