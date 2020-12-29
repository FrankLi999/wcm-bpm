import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { SharedUIModule } from "../../../common/shared-ui.module";

import { NavbarComponent } from "./navbar.component";
import { NavbarHorizontalModule } from "./horizontal/navbar-horizontal/navbar-horizontal.module";
import { NavbarVerticalModule } from "./vertical/navbar-vertical/navbar-vertical.module";

@NgModule({
  declarations: [NavbarComponent],
  imports: [
    CommonModule,
    SharedUIModule,

    NavbarHorizontalModule,
    NavbarVerticalModule,
  ],
  exports: [NavbarComponent],
})
export class NavbarModule {}
