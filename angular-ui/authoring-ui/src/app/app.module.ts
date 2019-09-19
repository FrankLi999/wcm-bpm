import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes, PreloadAllModules } from '@angular/router';
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
import { AppStoreModule } from 'app/store/store.module';
import { LayoutModule } from 'app/layout/layout.module';
import { RestClientModule } from './rest-client/rest-client.module';
import { FakeDbService } from 'app/fake-db/fake-db.service';
import { environment } from 'environments/environment';

const appRoutes: Routes = [
    {
        path        : 'wcm-authoring',
        loadChildren: './wcm-authoring/wcm-authoring.module#WcmAuthoringModule'
    },
    {
        path        : 'auth',
        loadChildren: './authentication/authentication.module#AuthenticationModule'
    },
    {
        path        : 'oauth2',
        loadChildren: './oauth2/oauth2.module#Oauth2Module'
    },
    {
        path        : 'bpmn',
        loadChildren: './bpmn/bpmn.module#BpmnModule'
    },
    {
        path      : '**',
        redirectTo: 'wcm-authoring'
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
        RouterModule.forRoot(appRoutes,
            environment.production ? {} : {
                enableTracing: false, // <-- debugging purposes only
                preloadingStrategy: PreloadAllModules
              }
        ),

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
        AppStoreModule,
		RestClientModule
    ],
    bootstrap   : [
        AppComponent
    ]
})
export class AppModule
{
}
