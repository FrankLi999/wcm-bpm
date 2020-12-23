import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterModule, Routes, PreloadAllModules } from "@angular/router";
import { MatMomentDateModule } from "@angular/material-moment-adapter";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { TranslateModule } from "@ngx-translate/core";
import { InMemoryWebApiModule } from "angular-in-memory-web-api";
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
  AuthStoreModule,
  AuthHttpInterceptor,
} from "bpw-common";
import { FakeDbService } from "./fake-db/fake-db.service";
import { environment } from "../environments/environment";
import { AppStoreModule } from "./store/store.module";
import { appApiConfig, appLayoutConfig } from "./config/app.config";
import { RestClientConfigModule } from "bpw-rest-client";
import { AppComponent } from "./app.component";
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
    path: "apps",
    loadChildren: () =>
      import("./main/apps/apps.module").then((m) => m.AppsModule),
    // loadChildren: './main/apps/apps.module#AppsModule'
  },
  {
    path: "pages",
    loadChildren: () =>
      import("./main/pages/pages.module").then((m) => m.PagesModule),
    // loadChildren: './main/pages/pages.module#PagesModule'
  },
  {
    path: "ui",
    loadChildren: () => import("./main/ui/ui.module").then((m) => m.UIModule),
    // loadChildren: './main/ui/ui.module#UIModule'
  },
  {
    path: "documentation",
    loadChildren: () =>
      import("./main/documentation/documentation.module").then(
        (m) => m.DocumentationModule
      ),
    // loadChildren: './main/documentation/documentation.module#DocumentationModule'
  },
  {
    path: "angular-material-elements",
    loadChildren: () =>
      import(
        "./main/angular-material-elements/angular-material-elements.module"
      ).then((m) => m.AngularMaterialElementsModule),
    // loadChildren: './main/angular-material-elements/angular-material-elements.module#AngularMaterialElementsModule'
  },
  {
    path: "**",
    redirectTo: "apps/dashboards/analytics",
  },
];
const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true,
};
@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule.forRoot(
      appRoutes,
      environment.production
        ? {}
        : {
            enableTracing: false, // <-- debugging purposes only
            preloadingStrategy: PreloadAllModules,
          }
    ),

    TranslateModule.forRoot(),
    InMemoryWebApiModule.forRoot(FakeDbService, {
      delay: 0,
      passThruUnknownUrl: true,
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
