import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { AgmCoreModule } from '@agm/core';

import { FuseSharedModule } from 'bpw-components';
import { FuseHighlightModule } from 'bpw-components';

import { DocsComponentsThirdPartyGoogleMapsComponent } from './google-maps.component';

const routes = [
    {
        path     : 'google-maps',
        component: DocsComponentsThirdPartyGoogleMapsComponent
    }
];

@NgModule({
    declarations: [
        DocsComponentsThirdPartyGoogleMapsComponent
    ],
    imports     : [
        RouterModule.forChild(routes),

        MatButtonModule,
        MatIconModule,

        AgmCoreModule.forRoot({
            apiKey: 'AIzaSyD81ecsCj4yYpcXSLFcYU97PvRsE_X8Bx8'
        }),

        FuseSharedModule,
        FuseHighlightModule
    ],
})
export class GoogleMapsModule
{
}
