import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';

import { DemoContentComponent } from './demo-content/demo-content.component';
import { DemoSidebarComponent } from './demo-sidebar/demo-sidebar.component';

@NgModule({
    declarations: [
        DemoContentComponent,
        DemoSidebarComponent
    ],
    imports     : [
        RouterModule,

        MatDividerModule,
        MatListModule
    ],
    exports     : [
        DemoContentComponent,
        DemoSidebarComponent
    ]
})
export class DemoModule {
}
