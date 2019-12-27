import { NgModule } from '@angular/core';

import { IfOnDomDirective } from './if-on-dom/if-on-dom.directive';
import { InnerScrollDirective } from './inner-scroll/inner-scroll.directive';
import { PerfectScrollbarDirective } from './perfect-scrollbar/perfect-scrollbar.directive';
import { MatSidenavHelperDirective, MatSidenavTogglerDirective } from './mat-sidenav/mat-sidenav.directive';
import { AceEditorDirective } from './ace/ace-editor.directive';

@NgModule({
    declarations: [
        IfOnDomDirective,
        InnerScrollDirective,
        MatSidenavHelperDirective,
        MatSidenavTogglerDirective,
        PerfectScrollbarDirective,
        AceEditorDirective
    ],
    imports     : [],
    exports     : [
        IfOnDomDirective,
        InnerScrollDirective,
        MatSidenavHelperDirective,
        MatSidenavTogglerDirective,
        PerfectScrollbarDirective,
        AceEditorDirective
    ]
})
export class UIDirectivesModule
{
}
