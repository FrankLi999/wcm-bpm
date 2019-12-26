import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { SharedUIModule } from '../../../../../common/shared-ui.module';
import { NavigationModule } from '../../../../../common/components/navigation/navigation.module';

import { NavbarVerticalStyle2Component } from './style-2.component';

@NgModule({
    declarations: [
        NavbarVerticalStyle2Component
    ],
    imports     : [
        CommonModule,
        MatButtonModule,
        MatIconModule,

        SharedUIModule,
        NavigationModule
    ],
    exports     : [
        NavbarVerticalStyle2Component
    ]
})
export class NavbarVerticalStyle2Module {
}
