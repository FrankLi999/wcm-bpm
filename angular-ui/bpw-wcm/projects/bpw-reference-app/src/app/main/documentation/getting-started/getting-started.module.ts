import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { SharedUIModule, HighlightModule } from 'bpw-common';

import { DocsGettingStartedIntroductionComponent } from './introduction/introduction.component';
import { DocsGettingStartedInstallationComponent } from './installation/installation.component';

const routes = [
    {
        path     : 'introduction',
        component: DocsGettingStartedIntroductionComponent
    },
    {
        path     : 'installation',
        component: DocsGettingStartedInstallationComponent
    }
];

@NgModule({
    declarations: [
        DocsGettingStartedIntroductionComponent,
        DocsGettingStartedInstallationComponent
    ],
    imports     : [
        RouterModule.forChild(routes),

        MatIconModule,

        SharedUIModule,
        HighlightModule
    ]
})
export class GettingStartedModule {
}
