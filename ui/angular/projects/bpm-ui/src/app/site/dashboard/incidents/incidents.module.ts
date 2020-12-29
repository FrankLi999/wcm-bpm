import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { IncidentsComponent } from "./incidents/incidents.component";

@NgModule({
  declarations: [IncidentsComponent],
  imports: [CommonModule],
  exports: [IncidentsComponent],
})
export class IncidentsModule {}
