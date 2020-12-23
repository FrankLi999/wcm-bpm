import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { SharedUIModule } from 'bpw-common';

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

        SharedUIModule
    ],
    providers   : [
        InvoiceService
    ]
})
export class InvoiceModernModule
{
}
