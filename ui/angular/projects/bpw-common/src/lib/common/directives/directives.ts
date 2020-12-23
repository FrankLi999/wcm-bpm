import { NgModule } from "@angular/core";

import { IfOnDomDirective } from "./if-on-dom/if-on-dom.directive";
import { InnerScrollDirective } from "./inner-scroll/inner-scroll.directive";
import {
  MatSidenavHelperDirective,
  MatSidenavTogglerDirective,
} from "./mat-sidenav/mat-sidenav.directive";
// import { AceEditorDirective } from "./ace/ace-editor.directive";
import { AutoFocusDirective } from "./focus/auto-focus.directive";

@NgModule({
  declarations: [
    IfOnDomDirective,
    InnerScrollDirective,
    MatSidenavHelperDirective,
    MatSidenavTogglerDirective,
    // AceEditorDirective,
    AutoFocusDirective,
  ],
  imports: [],
  exports: [
    IfOnDomDirective,
    InnerScrollDirective,
    MatSidenavHelperDirective,
    MatSidenavTogglerDirective,
    // AceEditorDirective,
    AutoFocusDirective,
  ],
})
export class UIDirectivesModule {}
