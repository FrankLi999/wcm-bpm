import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { SharedUIModule, HighlightModule } from 'bpw-common';
import { ConfigServiceDocsComponent } from './ui-config/config.component';
import { SplashScreenServiceDocsComponent } from './splash-screen/splash-screen.component';

const routes = [
    {
        path     : 'ui-config',
        component: ConfigServiceDocsComponent
    },
    {
        path     : 'splash-screen',
        component: SplashScreenServiceDocsComponent
    }
];

@NgModule({
    declarations: [
        ConfigServiceDocsComponent,
        SplashScreenServiceDocsComponent
    ],
    imports     : [
        RouterModule.forChild(routes),

        MatIconModule,

        SharedUIModule,
        HighlightModule
    ]
})

export class ServicesModule
{
}
