import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RegistrationStep1Component } from "./registration-step1/registration-step1.component";
import { RegistrationStep2Component } from "./registration-step2/registration-step2.component";
import { RegistrationStep3Component } from "./registration-step3/registration-step3.component";
import { RegistrationComponent } from "./registration.component";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatButtonModule } from "@angular/material/button";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatIconModule } from "@angular/material/icon";
import { MatListModule } from "@angular/material/list";
import { MatStepperModule } from "@angular/material/stepper";
import { MatInputModule } from "@angular/material/input";

@NgModule({
  declarations: [
    RegistrationStep1Component,
    RegistrationStep2Component,
    RegistrationStep3Component,
    RegistrationComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatStepperModule,
    MatToolbarModule,
  ],
  exports: [RegistrationComponent],
})
export class RegistrationModule {}
