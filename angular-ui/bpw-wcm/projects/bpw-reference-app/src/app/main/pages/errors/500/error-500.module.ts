import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedUIModule } from 'bpw-components';

import { Error500Component } from './error-500.component';

const routes = [
    {
        path     : 'errors/error-500',
        component: Error500Component
    }
];

@NgModule({
    declarations: [
        Error500Component
    ],
    imports     : [
        RouterModule.forChild(routes),

        SharedUIModule
    ]
})
export class Error500Module
{
}
