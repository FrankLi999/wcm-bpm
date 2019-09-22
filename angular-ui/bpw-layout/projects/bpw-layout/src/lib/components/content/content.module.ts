import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RouterModule } from '@angular/router';

import { FuseSharedModule } from 'bpw-components';

import { ContentComponent } from './content.component';

@NgModule({
    declarations: [
        ContentComponent
    ],
    imports     : [
        CommonModule,
        RouterModule,
        FuseSharedModule
    ],
    exports     : [
        ContentComponent
    ]
})
export class ContentModule
{
}
