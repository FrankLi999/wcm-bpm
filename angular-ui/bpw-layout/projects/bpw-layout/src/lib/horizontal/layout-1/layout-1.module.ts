import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSidenavModule } from '@angular/material/sidenav';

import { FuseSidebarModule, FuseThemeOptionsModule, FuseSharedModule } from 'bpw-components';

import { ContentModule } from '../../components/content/content.module';
import { FooterModule } from '../../components/footer/footer.module';
import { NavbarModule } from '../../components/navbar/navbar.module';
import { QuickPanelModule } from '../../components/quick-panel/quick-panel.module';
import { ToolbarModule } from '../../components/toolbar/toolbar.module';

import { HorizontalLayout1Component } from './layout-1.component';

@NgModule({
    declarations: [
        HorizontalLayout1Component
    ],
    imports     : [
        CommonModule,
        MatSidenavModule,

        FuseSharedModule,
        FuseSidebarModule,
        FuseThemeOptionsModule,

        ContentModule,
        FooterModule,
        NavbarModule,
        QuickPanelModule,
        ToolbarModule
    ],
    exports     : [
        HorizontalLayout1Component
    ]
})
export class HorizontalLayout1Module
{
}
