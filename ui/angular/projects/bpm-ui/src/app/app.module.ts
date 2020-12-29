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
import { appConfig } from "bpw-common";
appConfig.baseUrl = "bpm";
appConfig.defaultUrl = "/bpm/home";
appConfig.repository = "bpwizard",
appConfig.workspace = "default",
appConfig.library = "camunda";
appConfig.siteConfig = " bpm";
appConfig.apiBaseUrl = "http://wcm-server.bpwizard.com:28080",
appConfig.oauth2RedirectUrl = "http://bpm-ui:4009/oauth2/redirect";
appConfig.routeSubscriptions = [],
appConfig.layoutConfig = {
  // Color themes can be defined in src/app/app.theme.scss
  colorTheme: "theme-default",
  customScrollbars: true,
  layout: {
    title: "horizontal-layout",
    mode: "fullwidth",
    navbar: {
      primaryBackground: "wcm-light-200",
      secondaryBackground: "wcm-light-300",
      folded: false,
      display: false,
      position: "top",
      variant: "vertical",
    },
    toolbar: {
      customBackgroundColor: false,
      background: "wcm-light-300",
      display: false,
      position: "above",
    },
    footer: {
      customBackgroundColor: true,
      background: "wcm-light-300",
      display: false,
      position: "below-static",
    },
    leftSidePanel: {
      display: false,
    },
    rightSidePanel: {
      display: false,
    },
  },
};

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
      import("./wcm/wcm-lazy.module").then((m) => m.WcmLazyModule),
  },
  {
    path: "test-bpmn",
    loadChildren: () =>
      import("./bpmn-test/bpmn-test.module").then((m) => m.BpmnTestModule),
  },
  {
    path: "**",
    redirectTo: appConfig.defaultUrl,
  },
];

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  wheelPropagation: true,
};

@NgModule({
  declarations: [AppComponent, AppErrorComponent],
  imports: [
    BrowserModule,
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
    TranslateModule.forRoot(),
    MatMomentDateModule,
    MatButtonModule,
    MatIconModule,
    RestClientConfigModule.forRoot(appApiConfig),

    // // wcm modules
    UIConfigModule.forRoot(appLayoutConfig),
    ProgressBarModule,
    SharedUIModule,
    SidebarModule,
    ThemeOptionsModule,
    LayoutModule,
    AuthenticationModule,
    OAuth2Module,
    PerfectScrollbarModule,
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
