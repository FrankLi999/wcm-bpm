import { NgModule } from '@angular/core';

import { SidebarComponent } from './sidebar.component';

@NgModule({
    declarations: [
        SidebarComponent
    ],
    exports     : [
        SidebarComponent
    ]
})
export class SidebarModule {
}
