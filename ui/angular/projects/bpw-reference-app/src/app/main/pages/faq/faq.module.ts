import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';

import { SharedUIModule } from 'bpw-common';

import { FaqService } from './faq.service';
import { FaqComponent } from './faq.component';

const routes = [
    {
        path     : 'faq',
        component: FaqComponent,
        resolve  : {
            faq: FaqService
        }
    }
];

@NgModule({
    declarations: [
        FaqComponent
    ],
    imports     : [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule.forChild(routes),

        MatExpansionModule,
        MatIconModule,

        SharedUIModule
    ],
    providers   : [
        FaqService
    ]
})
export class FaqModule
{
}
