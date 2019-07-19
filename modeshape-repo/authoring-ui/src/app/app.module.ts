import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { MatMomentDateModule } from '@angular/material-moment-adapter';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { InMemoryWebApiModule } from 'angular-in-memory-web-api';
import { TranslateModule } from '@ngx-translate/core';
import 'hammerjs';

import { FuseModule } from '@fuse/fuse.module';
import { FuseSharedModule } from '@fuse/shared.module';
import { FuseProgressBarModule, FuseSidebarModule, FuseThemeOptionsModule } from '@fuse/components';

import { fuseConfig } from 'app/fuse-config';

import { AppComponent } from 'app/app.component';
import { LayoutModule } from 'app/layout/layout.module';
import { RestClientModule } from './main/rest-client/rest-client.module';
import { FakeDbService } from 'app/fake-db/fake-db.service';

const appRoutes: Routes = [
    {
        path        : 'wcm-authoring',
        loadChildren: './main/wcm-authoring/wcm-authoring.module#WcmAuthoringModule'
    },
    {
        path        : 'auth',
        loadChildren: './main/authentication/authentication.module#AuthenticationModule'
    },
    {
        path        : 'oauth2',
        loadChildren: './main/oauth2/oauth2.module#Oauth2Module'
    },
    {
        path        : 'bpmn',
        loadChildren: './main/bpmn/bpmn.module#BpmnModule'
    },
    {
        path      : '**',
        redirectTo: 'wcm-authoring/site-explorer'
    }
];

@NgModule({
    declarations: [
        AppComponent
    ],
    imports     : [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        RouterModule.forRoot(appRoutes),

        TranslateModule.forRoot(),
        InMemoryWebApiModule.forRoot(FakeDbService, {
            delay             : 0,
            passThruUnknownUrl: true
        }),
        // Material moment date module
        MatMomentDateModule,

        // Material
        MatButtonModule,
        MatIconModule,

        // Fuse modules
        FuseModule.forRoot(fuseConfig),
        FuseProgressBarModule,
        FuseSharedModule,
        FuseSidebarModule,
        FuseThemeOptionsModule,

        // App modules
        LayoutModule,
        // WcmAuthoringModule,
		RestClientModule
    ],
    bootstrap   : [
        AppComponent
    ]
})
export class AppModule
{
}
