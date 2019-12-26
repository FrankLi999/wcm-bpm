import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

import { SharedUIModule } from 'bpw-common';

import { ForgotPassword2Component } from './forgot-password-2.component';

const routes = [
    {
        path     : 'auth/forgot-password-2',
        component: ForgotPassword2Component
    }
];

@NgModule({
    declarations: [
        ForgotPassword2Component
    ],
    imports     : [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule.forChild(routes),

        MatButtonModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,

        SharedUIModule,
    ]
})
export class ForgotPassword2Module {
}
