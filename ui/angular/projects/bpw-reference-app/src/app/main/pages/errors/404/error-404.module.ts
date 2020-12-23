import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { SharedUIModule } from 'bpw-common';

import { Error404Component } from './error-404.component';

const routes = [
    {
        path     : 'errors/error-404',
        component: Error404Component
    }
];

@NgModule({
    declarations: [
        Error404Component
    ],
    imports     : [
        RouterModule.forChild(routes),

        MatIconModule,

        SharedUIModule
    ]
})
export class Error404Module {
}
