import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedUIModule } from 'bpw-common';

import { MaintenanceComponent } from './maintenance.component';

const routes = [
    {
        path     : 'maintenance',
        component: MaintenanceComponent
    }
];

@NgModule({
    declarations: [
        MaintenanceComponent
    ],
    imports     : [
        RouterModule.forChild(routes),

        SharedUIModule
    ]
})
export class MaintenanceModule
{
}
