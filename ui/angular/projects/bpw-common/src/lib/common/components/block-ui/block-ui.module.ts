import { NgModule } from "@angular/core";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";

import { BlockUIComponent } from "./block-ui.component";

@NgModule({
  imports: [MatProgressSpinnerModule],
  declarations: [BlockUIComponent],
  exports: [BlockUIComponent]
})
export class BlockUIModule {}
