import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { SharedUIModule } from '../../../common/shared-ui.module';
import { SidebarModule } from '../../../common/components/sidebar/sidebar.module';

import { ContentModule } from '../../components/content/content.module';
import { FooterModule } from '../../components/footer/footer.module';
import { NavbarModule } from '../../components/navbar/navbar.module';
import { QuickPanelModule } from '../../components/quick-panel/quick-panel.module';
import { ToolbarModule } from '../../components/toolbar/toolbar.module';

import { VerticalLayout2Component } from './layout-2.component';

@NgModule({
    declarations: [
        VerticalLayout2Component
    ],
    imports     : [
        CommonModule,
        RouterModule,

        SharedUIModule,
        SidebarModule,

        ContentModule,
        FooterModule,
        NavbarModule,
        QuickPanelModule,
        ToolbarModule
    ],
    exports     : [
        VerticalLayout2Component
    ]
})
export class VerticalLayout2Module {
}
