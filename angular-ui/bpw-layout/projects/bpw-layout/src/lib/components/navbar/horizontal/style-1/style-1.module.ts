import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { FuseNavigationModule, FuseSharedModule } from 'bpw-components';

import { NavbarHorizontalStyle1Component } from './style-1.component';

@NgModule({
    declarations: [
        NavbarHorizontalStyle1Component
    ],
    imports     : [
        CommonModule,
        MatButtonModule,
        MatIconModule,

        FuseSharedModule,
        FuseNavigationModule
    ],
    exports     : [
        NavbarHorizontalStyle1Component
    ]
})
export class NavbarHorizontalStyle1Module
{
}
