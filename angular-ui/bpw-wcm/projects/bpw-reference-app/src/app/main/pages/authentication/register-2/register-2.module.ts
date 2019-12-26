import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

import { SharedUIModule } from 'bpw-common';

import { Register2Component } from './register-2.component';

const routes = [
    {
        path     : 'auth/register-2',
        component: Register2Component
    }
];

@NgModule({
    declarations: [
        Register2Component
    ],
    imports     : [
        CommonModule,
        FormsModule, 
        ReactiveFormsModule,
        RouterModule.forChild(routes),

        MatButtonModule,
        MatCheckboxModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,

        SharedUIModule
    ]
})
export class Register2Module
{
}
