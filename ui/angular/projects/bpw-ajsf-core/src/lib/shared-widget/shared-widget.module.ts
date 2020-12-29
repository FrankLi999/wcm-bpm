import { BASIC_WIDGETS } from "./shared-widget";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { NgModule } from "@angular/core";
import { FlexLayoutModule } from "@angular/flex-layout";
import { OrderableDirective } from "./orderable.directive";
// import { AceEditorDirective } from "./ace-editor.directive";

@NgModule({
  imports: [CommonModule, FormsModule, ReactiveFormsModule, FlexLayoutModule],
  // declarations: [...BASIC_WIDGETS, OrderableDirective, AceEditorDirective],
  // exports: [...BASIC_WIDGETS, OrderableDirective, AceEditorDirective],
  declarations: [...BASIC_WIDGETS, OrderableDirective],
  exports: [...BASIC_WIDGETS, OrderableDirective],
})
export class SharedWidgetModule {}
