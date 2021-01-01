import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterModule, Routes, PreloadAllModules } from "@angular/router";
import { MatMomentDateModule } from "@angular/material-moment-adapter";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { TranslateModule } from "@ngx-translate/core";
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

import { environment } from "../environments/environment";
import { AppStoreModule } from "./store/store.module";


import { RestClientConfigModule } from "bpw-rest-client";

import { AppComponent } from "./app.component";
import { SampleModule } from "./sample/sample.module";
import { appConfig, layoutConfig } from "bpw-common";
import { appApiConfig, appLayoutConfig } from "./config/app.config";

const appRoutes: Routes = [
  {
    path: "**",
    redirectTo: "sample",
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
    PerfectScrollbarModule,
    TranslateModule.forRoot(),

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
    SampleModule,
    AuthStoreModule,
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
