import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { NgModule } from "@angular/core";
import { JsonSchemaFormComponent } from "./json-schema-form.component";
import { SharedWidgetModule } from "./shared-widget/shared-widget.module";
@NgModule({
  imports: [CommonModule, FormsModule, ReactiveFormsModule, SharedWidgetModule],
  declarations: [JsonSchemaFormComponent],
  exports: [JsonSchemaFormComponent, SharedWidgetModule]
})
export class JsonSchemaFormModule {}
