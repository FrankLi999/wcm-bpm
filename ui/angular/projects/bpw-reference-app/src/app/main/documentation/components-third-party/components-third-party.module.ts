import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';

import { SharedUIModule } from 'bpw-common';

import { GoogleMapsModule } from './google-maps/google-maps.module';
import { DocsComponentsThirdPartyNgxDatatableComponent } from './datatable/ngx-datatable.component';

const routes = [
    {
        path     : 'datatables/ngx-datatable',
        component: DocsComponentsThirdPartyNgxDatatableComponent
    }
];

@NgModule({
    declarations: [
        DocsComponentsThirdPartyNgxDatatableComponent
    ],
    imports     : [
        RouterModule.forChild(routes),

        MatButtonModule,
        MatCheckboxModule,
        MatIconModule,
        NgxDatatableModule,

        SharedUIModule,
        GoogleMapsModule
    ]
})
export class ComponentsThirdPartyModule {
}
