import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { SharedUIModule } from "../../../common/shared-ui.module";
import { SidebarModule } from "../../../common/components/sidebar/sidebar.module";

import { ContentModule } from "../../components/content/content.module";
import { FooterModule } from "../../components/footer/footer.module";
import { NavbarModule } from "../../components/navbar/navbar.module";
import { QuickPanelModule } from "../../components/quick-panel/quick-panel.module";
import { ToolbarModule } from "../../components/toolbar/toolbar.module";

import { VerticalLayoutComponent } from "./vertical-layout.component";

@NgModule({
  declarations: [VerticalLayoutComponent],
  imports: [
    CommonModule,
    RouterModule,

    SharedUIModule,
    SidebarModule,

    ContentModule,
    FooterModule,
    NavbarModule,
    QuickPanelModule,
    ToolbarModule,
    PerfectScrollbarModule,
  ],
  exports: [VerticalLayoutComponent],
})
export class VerticalLayoutModule {}
