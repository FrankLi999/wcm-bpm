import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';

import { FuseSharedModule } from 'bpw-components';
import { FuseDemoModule } from 'bpw-components';

import { ColorsComponent } from './colors.component';

const routes: Routes = [
    {
        path     : 'colors',
        component: ColorsComponent
    }
];

@NgModule({
    declarations: [
        ColorsComponent
    ],
    imports     : [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule.forChild(routes),

        MatButtonModule,
        MatIconModule,
        MatTabsModule,

        FuseSharedModule,
        FuseDemoModule
    ]
})
export class UIColorsModule
{
}
