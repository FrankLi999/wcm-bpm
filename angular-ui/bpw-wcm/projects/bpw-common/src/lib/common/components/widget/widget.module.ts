import { NgModule } from '@angular/core';

import { WidgetComponent } from './widget.component';
import { WidgetToggleDirective } from './widget-toggle.directive';

@NgModule({
    declarations: [
        WidgetComponent,
        WidgetToggleDirective
    ],
    exports     : [
        WidgetComponent,
        WidgetToggleDirective
    ],
})
export class WidgetModule { }
