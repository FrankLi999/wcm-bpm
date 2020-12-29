import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { RedirectHandlerComponent } from "./redirect-handler/redirect-handler.component";
import { RestClientModule } from "bpw-rest-client";
@NgModule({
  declarations: [RedirectHandlerComponent],
  imports: [RestClientModule, RouterModule],
  exports: [RedirectHandlerComponent],
})
export class Oauth2RedirectHandlerModule {}
