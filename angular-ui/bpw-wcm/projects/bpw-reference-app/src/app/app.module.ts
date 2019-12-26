import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes, PreloadAllModules } from '@angular/router';
import { MatMomentDateModule } from '@angular/material-moment-adapter';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TranslateModule } from '@ngx-translate/core';
import { InMemoryWebApiModule } from 'angular-in-memory-web-api';
import 'hammerjs';

import {
    UIConfigModule,
    SharedUIModule,
    ProgressBarModule,
    SidebarModule,
    ThemeOptionsModule,
    LayoutModule,
    AuthStoreModule,
    AuthHttpInterceptor 
} from 'bpw-common';
import { FakeDbService } from './fake-db/fake-db.service';
import { environment } from '../environments/environment';
import { AppStoreModule } from './store/store.module';
import { appApiConfig, appLayoutConfig} from './config/app.config';
import { RestClientConfigModule } from 'bpw-rest-client';
import { AppComponent } from './app.component';


const appRoutes: Routes = [
    {
        path        : 'apps',
        loadChildren: () => import('./main/apps/apps.module').then(m => m.AppsModule)
        // loadChildren: './main/apps/apps.module#AppsModule'
    },
    {
        path        : 'pages',
        loadChildren: () => import('./main/pages/pages.module').then(m => m.PagesModule)
        // loadChildren: './main/pages/pages.module#PagesModule'
    },
    {
        path        : 'ui',
        loadChildren: () => import('./main/ui/ui.module').then(m => m.UIModule)
        // loadChildren: './main/ui/ui.module#UIModule'
    },
    {
        path        : 'documentation',
        loadChildren: () => import('./main/documentation/documentation.module').then(m => m.DocumentationModule)
        // loadChildren: './main/documentation/documentation.module#DocumentationModule'
    },
    {
        path        : 'angular-material-elements',
        loadChildren: () => import('./main/angular-material-elements/angular-material-elements.module').then(m => m.AngularMaterialElementsModule)
        // loadChildren: './main/angular-material-elements/angular-material-elements.module#AngularMaterialElementsModule'
    },
    {
        path      : '**',
        redirectTo: 'apps/dashboards/analytics'
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
        RestClientConfigModule.forRoot(appApiConfig),

        // WCM modules
        UIConfigModule.forRoot(appLayoutConfig),
        ProgressBarModule,
        SharedUIModule,
        SidebarModule,
        ThemeOptionsModule,

        // App modules
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
