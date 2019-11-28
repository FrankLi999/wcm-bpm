import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { FuseSharedModule } from 'bpw-components';
import { FuseHighlightModule } from 'bpw-components';
import { FuseConfigServiceDocsComponent } from './fuse-config/fuse-config.component';
import { FuseSplashScreenServiceDocsComponent } from './fuse-splash-screen/fuse-splash-screen.component';

const routes = [
    {
        path     : 'fuse-config',
        component: FuseConfigServiceDocsComponent
    },
    {
        path     : 'fuse-splash-screen',
        component: FuseSplashScreenServiceDocsComponent
    }
];

@NgModule({
    declarations: [
        FuseConfigServiceDocsComponent,
        FuseSplashScreenServiceDocsComponent
    ],
    imports     : [
        RouterModule.forChild(routes),

        MatIconModule,

        FuseSharedModule,
        FuseHighlightModule
    ]
})

export class ServicesModule
{
}
