import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FlexComponent } from "./flex/flex.component";

@NgModule({
  declarations: [FlexComponent],
  imports: [CommonModule],
  exports: [FlexComponent],
})
export class FlexModule {}
