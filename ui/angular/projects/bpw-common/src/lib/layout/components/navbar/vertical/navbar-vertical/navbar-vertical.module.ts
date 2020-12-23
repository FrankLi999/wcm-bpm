import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { SharedUIModule } from "../../../../../common/shared-ui.module";
import { NavigationModule } from "../../../../../common/components/navigation/navigation.module";
import { NavbarVerticalComponent } from "./navbar-vertical.component";

@NgModule({
  declarations: [NavbarVerticalComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,

    SharedUIModule,
    NavigationModule,
    PerfectScrollbarModule,
  ],
  exports: [NavbarVerticalComponent],
})
export class NavbarVerticalModule {}
