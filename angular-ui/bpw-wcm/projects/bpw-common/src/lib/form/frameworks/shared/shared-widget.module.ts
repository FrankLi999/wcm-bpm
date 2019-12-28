import { ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { JsonSchemaFormService } from '../../json-schema-form.service';
import { OrderableDirective } from './orderable.directive';
import { BASIC_WIDGETS } from './shared-widget';

@NgModule({
  imports:         [ CommonModule, FormsModule, ReactiveFormsModule ],
  declarations:    [ ...BASIC_WIDGETS, OrderableDirective ],
  exports:         [ ...BASIC_WIDGETS, OrderableDirective ],
  entryComponents: [ ...BASIC_WIDGETS ],
  providers:       [ JsonSchemaFormService ]
})
export class SharedWidgetModule {
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: SharedWidgetModule,
      providers: [ JsonSchemaFormService ]
    };
  }
}
