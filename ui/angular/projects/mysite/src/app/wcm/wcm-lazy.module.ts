import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FlexLayoutModule } from "@angular/flex-layout";

import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";

import { MatInputModule } from "@angular/material/input";
import { MatMenuModule } from "@angular/material/menu";
import { MatMomentDateModule } from "@angular/material-moment-adapter";
import { MatProgressBarModule } from "@angular/material/progress-bar";
import { MatRippleModule } from "@angular/material/core";
import { MatSelectModule } from "@angular/material/select";
import { MatToolbarModule } from "@angular/material/toolbar";

import { TranslateModule } from "@ngx-translate/core";
// import { AceEditorModule } from "ng2-ace-editor";

import {
  SharedUIModule,
  ProgressBarModule,
  SidebarModule,
  ThemeOptionsModule,
  LayoutModule,
  AuthGuard,
} from "bpw-common";
import { JsonSchemaFormModule } from "@bpw/ajsf-core";
import { MaterialDesignFrameworkModule } from "@bpw/ajsf-material";
import { WcmAppStoreModule, ResolveGuard } from "bpw-wcm-service";
import { WcmRenderingModule, WcmPageComponent } from "bpw-wcm-rendering";
import { WcmRenderingComponent } from "./entry/wcm-rendering/wcm-rendering.component";
const routes: Routes = [
  {
    path: "",
    component: WcmRenderingComponent,
    canActivate: [AuthGuard, ResolveGuard],
    children: [
      {
        path: "**",
        component: WcmPageComponent,
      },
    ],
  },
];
@NgModule({
  declarations: [WcmRenderingComponent],
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    // AceEditorModule,
    TranslateModule,

    MatButtonModule,
    MatCheckboxModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,

    MatMenuModule,
    MatMomentDateModule,
    MatProgressBarModule,
    MatRippleModule,
    MatSelectModule,
    MatToolbarModule,

    SharedUIModule,
    SidebarModule,
    ProgressBarModule,
    ThemeOptionsModule,
    JsonSchemaFormModule,
    MaterialDesignFrameworkModule,
    LayoutModule,
    WcmAppStoreModule,
    WcmRenderingModule
  ],
  providers: [ResolveGuard],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class WcmLazyModule {}
