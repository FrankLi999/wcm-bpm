import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { PositionsComponent } from "./positions/positions.component";

@NgModule({
  declarations: [PositionsComponent],
  imports: [CommonModule],
  exports: [PositionsComponent],
})
export class PositionModule {}
