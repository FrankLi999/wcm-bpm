import { NgModule, CUSTOM_ELEMENTS_SCHEMA, Injector } from "@angular/core";
import { CommonModule } from "@angular/common";
import { createCustomElement } from "@angular/elements";
import { RouterModule } from "@angular/router";
import { BpmApplicationsComponent } from "./bpm-applications/bpm-applications.component";
import { BpmLinksComponent } from "./bpm-links/bpm-links.component";
import { UserProfileComponent } from "./user-profile/user-profile.component";

@NgModule({
  declarations: [
    BpmApplicationsComponent,
    BpmLinksComponent,
    UserProfileComponent,
  ],
  imports: [CommonModule, RouterModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [BpmApplicationsComponent, BpmLinksComponent, UserProfileComponent],
})
export class SiteHomeModule {
  constructor(private injector: Injector) {
    const bpmApplicationsComponent = createCustomElement(
      BpmApplicationsComponent,
      { injector: injector }
    );
    const bpmLinksComponent = createCustomElement(BpmLinksComponent, {
      injector: injector,
    });
    const userProfileComponent = createCustomElement(UserProfileComponent, {
      injector: injector,
    });
    customElements.define("bpm-applications", bpmApplicationsComponent);
    customElements.define("bpm-links", bpmLinksComponent);
    customElements.define("user-profile", userProfileComponent);
  }
}
