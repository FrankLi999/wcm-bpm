import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { CdkTreeModule } from "@angular/cdk/tree";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { MatButtonModule } from "@angular/material/button";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatCardModule } from "@angular/material/card";
import { MatRippleModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatMenuModule } from "@angular/material/menu";
import { MatSelectModule } from "@angular/material/select";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatTreeModule } from "@angular/material/tree";

import { MatChipsModule } from "@angular/material/chips";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatSortModule } from "@angular/material/sort";
import { MatTableModule } from "@angular/material/table";
import { MatTabsModule } from "@angular/material/tabs";

import { TranslateModule } from "@ngx-translate/core";
import { ComponentModule } from "../components/component.module";
import { AclModule } from "../components/acl/acl.module";
import { HistoryModule } from "../components/history/history.module";
import { SharedUIModule, SidebarModule } from "bpw-common";
import { JsonSchemaFormModule } from "@bpw/ajsf-core";
import { MaterialDesignFrameworkModule } from "@bpw/ajsf-material";

import { SiteConfigComponent } from "./site-config/site-config.component";
import { SiteConfigsComponent } from "./site-configs/site-configs.component";
import { SiteConfigTreeComponent } from "./site-config-tree/site-config-tree.component";
import { WcmFormWidgetModule } from "../wcm-form-widget/wcm-form-widget.module";

@NgModule({
  declarations: [
    SiteConfigComponent,
    SiteConfigsComponent,
    SiteConfigTreeComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    CdkTreeModule,
    ScrollingModule,
    MatButtonModule,
    MatCheckboxModule,
    MatCardModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatRippleModule,
    MatSelectModule,
    MatToolbarModule,
    MatChipsModule,
    MatExpansionModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatSortModule,
    MatSnackBarModule,
    MatTableModule,
    MatTabsModule,
    MatTreeModule,
    TranslateModule.forChild({
      extend: true,
    }),
    SharedUIModule,
    SidebarModule,
    JsonSchemaFormModule,
    MaterialDesignFrameworkModule,
    ComponentModule,
    AclModule,
    HistoryModule,
    WcmFormWidgetModule,
  ],
  exports: [SiteConfigComponent, SiteConfigsComponent, SiteConfigTreeComponent],
})
export class SiteConfigModule {}
