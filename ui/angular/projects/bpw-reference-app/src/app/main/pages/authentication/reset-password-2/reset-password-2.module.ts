import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { RouterModule } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";

import { SharedUIModule } from "bpw-common";

import { ResetPassword2Component } from "./reset-password-2.component";

const routes = [
  {
    path: "auth/reset-password-2",
    component: ResetPassword2Component,
  },
];

@NgModule({
  declarations: [ResetPassword2Component],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),

    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    PerfectScrollbarModule,
    SharedUIModule,
  ],
})
export class ResetPassword2Module {}
