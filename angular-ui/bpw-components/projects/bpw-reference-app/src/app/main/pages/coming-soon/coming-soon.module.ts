import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

import { FuseSharedModule } from 'bpw-components';
import { FuseCountdownModule } from 'bpw-components';

import { ComingSoonComponent } from './coming-soon.component';

const routes = [
    {
        path     : 'coming-soon',
        component: ComingSoonComponent
    }
];

@NgModule({
    declarations: [
        ComingSoonComponent
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

        FuseSharedModule,
        FuseCountdownModule
    ]
})
export class ComingSoonModule
{
}
