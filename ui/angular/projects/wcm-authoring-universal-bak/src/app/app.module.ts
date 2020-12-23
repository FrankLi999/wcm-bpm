import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterModule, Routes, PreloadAllModules } from "@angular/router";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatDialogModule } from "@angular/material/dialog";
import { TranslateModule } from "@ngx-translate/core";

import { MatMomentDateModule } from "@angular/material-moment-adapter";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { FlexLayoutModule } from "@angular/flex-layout";

import {
  PerfectScrollbarModule,
  PerfectScrollbarConfigInterface,
  PERFECT_SCROLLBAR_CONFIG,
} from "ngx-perfect-scrollbar";

import {
  UIConfigModule,
  SharedUIModule,
  ProgressBarModule,
  SidebarModule,
  ThemeOptionsModule,
  LayoutModule,
  AuthHttpInterceptor,
} from "bpw-common";

import { AuthenticationModule, OAuth2Module } from "bpw-auth";

import { RestClientConfigModule } from "bpw-rest-client";
import { environment } from "../environments/environment";
import { AppComponent } from "./app.component";
import { AppStoreModule } from "./store/store.module";

import { appApiConfig, appLayoutConfig } from "./config/app.config";
import { AppErrorComponent } from "./components/app-error/app-error.component";
import { appConfig, layoutConfig } from "bpw-common";

appConfig.baseUrl = "wcm-authoring";
appConfig.defaultUrl = "/wcm-authoring/site-explorer";
appConfig.repository = "bpwizard",
appConfig.workspace = "default",
appConfig.library = "camunda";
appConfig.siteConfig = " bpm";
appConfig.apiBaseUrl = "http://wcm-server.bpwizard.com:28080",
appConfig.oauth2RedirectUrl = "http://wcm-authoring:3009/oauth2/redirect";
appConfig.routeSubscriptions = [],
appConfig.layoutConfig = layoutConfig;

const appRoutes: Routes = [
  {
    path: "auth",
    loadChildren: () =>
      import("./auth/auth-lazy.module").then((m) => m.AuthenticationLazyModule),
  },
  {
    path: "oauth2",
    loadChildren: () =>
      import("./oauth2/oauth2-lazy.module").then((m) => m.Oauth2LazyModule),
  },
  {
    path: "app-error",
    component: AppErrorComponent,
  },
  {
    path: appConfig.baseUrl,
    loadChildren: () =>
      import("./wcm/wcm-authoring-lazy.module").then(
        (m) => m.WcmAuthoringLazyModule
      ),
  },
  {
    path: "**",
    redirectTo: appConfig.defaultUrl,
  },
];

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true,
};

@NgModule({
  declarations: [AppComponent, AppErrorComponent],
  imports: [
    // BrowserModule,
    BrowserModule.withServerTransition({ appId: 'wcm-authoring' }),
    BrowserAnimationsModule,
    RouterModule.forRoot(
      appRoutes,
      environment.production
        ? {}
        : {
            enableTracing: false, // <-- debugging purposes only
            preloadingStrategy: PreloadAllModules,
          }
    ),
    HttpClientModule,
    MatSnackBarModule,
    MatDialogModule,
    FlexLayoutModule,
    PerfectScrollbarModule,
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
    AuthenticationModule,
    OAuth2Module,
    AppStoreModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthHttpInterceptor,
      multi: true,
    },
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG,
    },
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent],
})
export class AppModule {}
