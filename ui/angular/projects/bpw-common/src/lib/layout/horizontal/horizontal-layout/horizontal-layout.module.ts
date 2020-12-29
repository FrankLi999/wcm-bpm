import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MatSidenavModule } from "@angular/material/sidenav";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { SharedUIModule } from "../../../common/shared-ui.module";
import { ThemeOptionsModule } from "../../../common/components/theme-options/theme-options.module";
import { SidebarModule } from "../../../common/components/sidebar/sidebar.module";

import { ContentModule } from "../../components/content/content.module";
import { FooterModule } from "../../components/footer/footer.module";
import { NavbarModule } from "../../components/navbar/navbar.module";
import { QuickPanelModule } from "../../components/quick-panel/quick-panel.module";
import { ToolbarModule } from "../../components/toolbar/toolbar.module";

import { HorizontalLayoutComponent } from "./horizontal-layout.component";

@NgModule({
  declarations: [HorizontalLayoutComponent],
  imports: [
    CommonModule,
    MatSidenavModule,

    SharedUIModule,
    SidebarModule,
    ThemeOptionsModule,

    ContentModule,
    FooterModule,
    NavbarModule,
    QuickPanelModule,
    ToolbarModule,
    PerfectScrollbarModule,
  ],
  exports: [HorizontalLayoutComponent],
})
export class HorizontalLayoutModule {}
