import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { SharedUIModule } from '../../../common/shared-ui.module';
import { ContentComponent } from './content.component';

@NgModule({
    declarations: [
        ContentComponent
    ],
    imports     : [
        CommonModule,
        RouterModule,
        SharedUIModule
    ],
    exports     : [
        ContentComponent
    ]
})
export class ContentModule{ }
