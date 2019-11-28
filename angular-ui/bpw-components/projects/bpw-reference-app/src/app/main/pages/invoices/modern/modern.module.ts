import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { FuseSharedModule } from 'bpw-components';

import { InvoiceService } from '../invoice.service';
import { InvoiceModernComponent } from '../modern/modern.component';

const routes = [
    {
        path     : 'invoices/modern',
        component: InvoiceModernComponent,
        resolve  : {
            search: InvoiceService
        }
    }
];

@NgModule({
    declarations: [
        InvoiceModernComponent
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
export class InvoiceModernModule
{
}
