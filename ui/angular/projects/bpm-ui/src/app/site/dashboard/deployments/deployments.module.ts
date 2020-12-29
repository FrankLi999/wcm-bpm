import { NgModule, CUSTOM_ELEMENTS_SCHEMA, Injector } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";

import { DeploymentsComponent } from "./deployments/deployments.component";

@NgModule({
  declarations: [DeploymentsComponent],
  imports: [CommonModule, RouterModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [DeploymentsComponent],
})
export class DeploymentsModule {}
