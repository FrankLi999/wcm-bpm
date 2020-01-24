import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { JsonSchemaFormComponent } from './json-schema-form.component';
import { NgModule } from '@angular/core';
import { MaterialDesignFrameworkModule } from './frameworks/material/material-design-framework.module';
import { SharedWidgetModule } from './frameworks/shared/shared-widget.module';
import { fixAngularFlex } from './angular-flex-monkey-patch';


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SharedWidgetModule,
    MaterialDesignFrameworkModule
  ],
  declarations: [JsonSchemaFormComponent],
  exports: [JsonSchemaFormComponent]
})
export class JsonSchemaFormModule {
  constructor() {
    fixAngularFlex();
  }
}
