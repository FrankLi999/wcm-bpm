import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";

import { SharedUIModule } from "../../../../../common/shared-ui.module";
import { NavigationModule } from "../../../../../common/components/navigation/navigation.module";

import { NavbarHorizontalComponent } from "./navbar-horizontal.component";

@NgModule({
  declarations: [NavbarHorizontalComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,

    SharedUIModule,
    NavigationModule,
  ],
  exports: [NavbarHorizontalComponent],
})
export class NavbarHorizontalModule {}
