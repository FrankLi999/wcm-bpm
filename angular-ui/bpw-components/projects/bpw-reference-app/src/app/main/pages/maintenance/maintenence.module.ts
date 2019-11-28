import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FuseSharedModule } from 'bpw-components';

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

        FuseSharedModule
    ]
})
export class MaintenanceModule
{
}
