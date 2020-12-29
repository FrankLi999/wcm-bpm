import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { DecisionsComponent } from "./decisions/decisions.component";
import { DecisionDefinitionRuntimeComponent } from "./decision-definition-runtime/decision-definition-runtime.component";
import { DecisionInstanceComponent } from "./decision-instance/decision-instance.component";

@NgModule({
  declarations: [
    DecisionsComponent,
    DecisionDefinitionRuntimeComponent,
    DecisionInstanceComponent,
  ],
  imports: [CommonModule],
  exports: [
    DecisionsComponent,
    DecisionDefinitionRuntimeComponent,
    DecisionInstanceComponent,
  ],
})
export class DecisionsModule {}
