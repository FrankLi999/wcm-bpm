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
  UIConfigModule,
  SharedUIModule,
  ProgressBarModule,
  SidebarModule,
  ThemeOptionsModule,
  LayoutModule,
  AuthStoreModule,
  AuthHttpInterceptor
} from 'bpw-components';
import { RestClientConfigModule } from 'bpw-rest-client';
import { environment } from '../environments/environment';
import { AppComponent } from './app.component';
import { AppStoreModule } from './store/store.module';

import { appApiConfig, appLayoutConfig} from './config/app.config';
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
        path      : appConfig.baseUrl,
        loadChildren: () => import('./wcm/wcm-authoring-lazy.module').then(m => m.WcmAuthoringLazyModule)
    },
    {
        path      : '**',
        redirectTo: appConfig.defaultUrl
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

        // // WCM modules
        UIConfigModule.forRoot(appLayoutConfig),
        ProgressBarModule,
        SharedUIModule,
        SidebarModule,
        ThemeOptionsModule,
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
