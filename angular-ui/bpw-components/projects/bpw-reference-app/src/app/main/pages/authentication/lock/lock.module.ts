import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

import { FuseSharedModule } from 'bpw-components';

import { LockComponent } from './lock.component';

const routes = [
    {
        path     : 'auth/lock',
        component: LockComponent
    }
];

@NgModule({
    declarations: [
        LockComponent
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

        FuseSharedModule
    ]
})
export class LockModule
{
}