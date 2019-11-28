import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { FuseSharedModule } from 'bpw-components';

import { InvoiceService } from '../invoice.service';
import { InvoiceCompactComponent } from './compact.component';

const routes = [
    {
        path     : 'invoices/compact',
        component: InvoiceCompactComponent,
        resolve  : {
            search: InvoiceService
        }
    }
];

@NgModule({
    declarations: [
        InvoiceCompactComponent
    ],
    imports     : [
        CommonModule,
        RouterModule.forChild(routes),

        FuseSharedModule
    ],
    providers   : [
        InvoiceService
    ]
})
export class InvoiceCompactModule
{
}
