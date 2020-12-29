import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { FlexLayoutModule } from "@angular/flex-layout";
import { CdkTableModule } from "@angular/cdk/table";
import { CdkTreeModule } from "@angular/cdk/tree";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatRippleModule } from "@angular/material/core";
import { MatDialogModule } from "@angular/material/dialog";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatMenuModule } from "@angular/material/menu";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatSelectModule } from "@angular/material/select";
import { MatSortModule } from "@angular/material/sort";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatTableModule } from "@angular/material/table";
import { MatTreeModule } from "@angular/material/tree";
import { TranslateModule } from "@ngx-translate/core";
import { SharedUIModule, SidebarModule } from "bpw-common";

import { ResourceRendererComponent } from "./resource-renderer/resource-renderer.component";
import { WcmElementsModule } from "bpw-wcm-elements";
import { KeepHtmlPipe } from "./keep-html.pipe";
import { ContentAreaRendererComponent } from "./content-area-renderer/content-area-renderer.component";
import { ContentIdDirective } from "./wcm-plugin/content-id.directive";
import { WcmPageComponent } from "./wcm-page/wcm-page.component";
import { QueryResultRendererComponent } from './query-result-renderer/query-result-renderer.component';
import { QueryRowRendererComponent } from './query-row-renderer/query-row-renderer.component';
@NgModule({
  declarations: [
    ResourceRendererComponent,
    KeepHtmlPipe,
    ContentAreaRendererComponent,
    WcmPageComponent,
    ContentIdDirective,
    QueryResultRendererComponent,
    QueryRowRendererComponent,
  ],
  imports: [
    RouterModule,
    CommonModule,
    CdkTableModule,
    CdkTreeModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatDialogModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatPaginatorModule,
    MatRippleModule,
    MatSelectModule,
    MatSortModule,
    MatTableModule,
    MatToolbarModule,
    MatTreeModule,

    FlexLayoutModule,
    TranslateModule.forChild({
      extend: true,
    }),
    SharedUIModule,
    SidebarModule,
    WcmElementsModule,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [
    ResourceRendererComponent,
    KeepHtmlPipe,
    ContentIdDirective,
    WcmPageComponent,
  ],
})
export class WcmRenderingModule {}
