import { Directive, HostListener } from "@angular/core";
import { WcmEditableComponent } from "./wcm-editable.component";
@Directive({
  selector: "[editableOnEnter]"
})
export class EditableOnEnterDirective {
  constructor(private editable: WcmEditableComponent) {}

  @HostListener("keyup.enter")
  onEnter() {
    this.editable.toViewMode();
  }
}
