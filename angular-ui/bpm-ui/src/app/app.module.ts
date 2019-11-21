import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes, PreloadAllModules } from '@angular/router';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

import 'hammerjs';

import { MatMomentDateModule } from '@angular/material-moment-adapter';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FlexLayoutModule } from '@angular/flex-layout';

import {
  FuseConfigModule,
  FuseSharedModule,
  FuseProgressBarModule,
  FuseSidebarModule,
  FuseThemeOptionsModule
} from 'bpw-components';


import { LayoutModule } from 'bpw-layout';
import { AuthStoreModule, AuthHttpInterceptor } from 'bpw-auth-store';

import { environment } from '../environments/environment';
import { AppComponent } from './app.component';
import { AppStoreModule } from './store/store.module';

import { appApiConfig, appLayoutConfig} from './config/app.config';
import { RestClientConfigModule } from 'bpw-rest-client';
declare var appConfig: any;
const appRoutes: Routes = [
    {
        path        : 'auth',
        loadChildren: () => import('./auth/auth-lazy.module').then(m => m.AuthenticationLazyModule)
    },
    {
        path        : 'oauth2',
        loadChildren: () => import('./oauth2/oauth2-lazy.module').then(m => m.Oauth2LazyModule)
    },
    {
        path        : 'bpmn',
        loadChildren: () => import('./bpmn/bpmn.module').then(m => m.BpmnModule)
    },
    {
        path      : '**',
        redirectTo: appConfig.baseUrl
    }
];

@NgModule({
    declarations: [
        AppComponent
    ],
    imports     : [
        BrowserModule,
        BrowserAnimationsModule,
        RouterModule.forRoot(appRoutes,
            environment.production ? {} : {
                enableTracing: false, // <-- debugging purposes only
                preloadingStrategy: PreloadAllModules
              }
        ),
        HttpClientModule,
        MatSnackBarModule,
        MatDialogModule,
        FlexLayoutModule,
        TranslateModule.forRoot(),
        MatMomentDateModule,
        MatButtonModule,
        MatIconModule,
        RestClientConfigModule.forRoot(appApiConfig),

        // // Fuse modules
        FuseConfigModule.forRoot(appLayoutConfig),
        FuseProgressBarModule,
        FuseSharedModule,
        FuseSidebarModule,
        FuseThemeOptionsModule,
        LayoutModule,
        AuthStoreModule,
        AppStoreModule
    ],
    providers: [{
        provide: HTTP_INTERCEPTORS,
        useClass: AuthHttpInterceptor,
        multi: true
      }
    ],
    schemas: [
        CUSTOM_ELEMENTS_SCHEMA
    ],
    bootstrap   : [
        AppComponent
    ]
})
export class AppModule {
}
