import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { FuseNavigationModule, FuseSharedModule } from 'bpw-components';

import { NavbarVerticalStyle1Component } from './style-1.component';

@NgModule({
    declarations: [
        NavbarVerticalStyle1Component
    ],
    imports     : [
        CommonModule,
        MatButtonModule,
        MatIconModule,

        FuseSharedModule,
        FuseNavigationModule
    ],
    exports     : [
        NavbarVerticalStyle1Component
    ]
})
export class NavbarVerticalStyle1Module
{
}
