import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

import { SharedUIModule } from '../../../common/shared-ui.module';

import { QuickPanelComponent } from './quick-panel.component';

@NgModule({
    declarations: [
        QuickPanelComponent
    ],
    imports     : [
        CommonModule,
        FormsModule, 
        ReactiveFormsModule,
        MatDividerModule,
        MatListModule,
        MatSlideToggleModule,

        SharedUIModule,
    ],
    exports: [
        QuickPanelComponent
    ]
})
export class QuickPanelModule {
}
