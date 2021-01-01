import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { CommonModule } from "@angular/common";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterModule, Routes, PreloadAllModules } from "@angular/router";
import { MatMomentDateModule } from "@angular/material-moment-adapter";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { TranslateModule } from "@ngx-translate/core";

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

import { AppStoreModule } from "./store/store.module";
import { RestClientConfigModule } from "bpw-rest-client";
import { AppComponent } from "./app.component";
import { SampleModule } from "./sample/sample.module";
import { PositionModule } from "./poc/position/position.module";
import { FlexModule } from "./poc/flex/flex.module";
import { PositionsComponent } from "./poc/position/positions/positions.component";
import { FlexComponent } from "./poc/flex/flex/flex.component";
import { appApiConfig, appLayoutConfig } from "./config/app.config";
const appRoutes: Routes = [
  {
    path: "forms",
    loadChildren: () =>
      import("./forms/app-forms.module").then((m) => m.AppFormsModule),
  },
  {
    path: "grid",
    loadChildren: () =>
      import("./poc/grid/grid.module").then((m) => m.GridModule),
  },
  {
    path: "positions",
    component: PositionsComponent,
  },
  {
    path: "flex",
    component: FlexComponent,
  },
  {
    path: "**",
    redirectTo: "grid/home",
  },
];

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    CommonModule,
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),

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
    PositionModule,
    FlexModule,
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
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent],
})
export class AppModule {}
