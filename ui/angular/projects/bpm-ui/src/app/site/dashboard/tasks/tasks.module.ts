import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AssignedByTypeComponent } from "./assigned-by-type/assigned-by-type.component";
import { AssignedByGroupComponent } from "./assigned-by-group/assigned-by-group.component";

@NgModule({
  declarations: [AssignedByTypeComponent, AssignedByGroupComponent],
  imports: [CommonModule],
  exports: [AssignedByTypeComponent, AssignedByGroupComponent],
})
export class TasksModule {}
