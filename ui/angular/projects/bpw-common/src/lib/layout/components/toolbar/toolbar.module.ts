import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { FlexLayoutModule } from '@angular/flex-layout';
import { SharedUIModule } from '../../../common/shared-ui.module';
import { SearchBarModule } from '../../../common/components/search-bar/search-bar.module';
import { ShortcutsModule } from '../../../common/components/shortcuts/shortcuts.module';

import { ToolbarComponent } from './toolbar.component';

@NgModule({
    declarations: [
        ToolbarComponent
    ],
    imports     : [
        CommonModule,
        RouterModule,
        MatButtonModule,
        MatIconModule,
        MatMenuModule,
        MatToolbarModule,
        FlexLayoutModule,
        
        SharedUIModule,
        SearchBarModule,
        ShortcutsModule
    ],
    exports     : [
        ToolbarComponent
    ]
})
export class ToolbarModule {
}
