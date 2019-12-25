import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { SharedUIModule } from '../../../../../common/shared-ui.module';
import { NavigationModule } from '../../../../../common/components/navigation/navigation.module';


import { NavbarHorizontalStyle1Component } from './style-1.component';

@NgModule({
    declarations: [
        NavbarHorizontalStyle1Component
    ],
    imports     : [
        CommonModule,
        MatButtonModule,
        MatIconModule,

        SharedUIModule,
        NavigationModule
    ],
    exports     : [
        NavbarHorizontalStyle1Component
    ]
})
export class NavbarHorizontalStyle1Module {
}
