import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { VerticalLayoutModule } from "./vertical/vertical-layout/vertical-layout.module";
import { ProgressBarModule } from "../common/components/progress-bar/progress-bar.module";
import { SidebarModule } from "../common/components/sidebar/sidebar.module";
import { ThemeOptionsModule } from "../common/components/theme-options/theme-options.module";
import { HorizontalLayoutModule } from "./horizontal/horizontal-layout/horizontal-layout.module";
import { AppConfigStoreModule } from "./store/store.module";
import { PageLayoutComponent } from "./page-layout/page-layout.component";
import { CustomizablePageLayoutComponent } from "./customizable-page-layout/customizable-page-layout.component";

@NgModule({
  imports: [
    CommonModule,
    VerticalLayoutModule,
    ProgressBarModule,
    HorizontalLayoutModule,
    SidebarModule,
    ThemeOptionsModule,
    AppConfigStoreModule,
    MatButtonModule,
    MatIconModule,
  ],
  exports: [
    VerticalLayoutModule,
    HorizontalLayoutModule,
    PageLayoutComponent,
    CustomizablePageLayoutComponent,
  ],
  declarations: [PageLayoutComponent, CustomizablePageLayoutComponent],
})
export class LayoutModule {}
