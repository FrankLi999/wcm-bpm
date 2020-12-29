import {
  Component,
  OnInit,
  Output,
  EventEmitter,
  ContentChild,
  ElementRef,
  ViewChild,
} from "@angular/core";
import { Subject, fromEvent } from "rxjs";
import { filter, take, switchMapTo } from "rxjs/operators";
import { UntilDestroy, untilDestroyed } from "@ngneat/until-destroy";
import { ViewModeDirective } from "./view-mode.directive";
import { EditModeDirective } from "./edit-mode.directive";

// https://indepth.dev/exploring-angular-dom-manipulation-techniques-using-viewcontainerref/
@UntilDestroy()
@Component({
  selector: "wcm-editable",
  templateUrl: "./wcm-editable.component.html",
  styleUrls: ["./wcm-editable.component.scss"],
})
export class WcmEditableComponent implements OnInit {
  @Output() update = new EventEmitter();
  @ContentChild(ViewModeDirective) viewModeTpl: ViewModeDirective;
  @ContentChild(EditModeDirective) editModeTpl: EditModeDirective;

  editMode = new Subject();
  editMode$ = this.editMode.asObservable();
  mode: "view" | "edit" = "view";

  constructor(private host: ElementRef) {}

  ngOnInit() {
    this.viewModeHandler();
    this.editModeHandler();
  }

  private get element() {
    return this.host.nativeElement;
  }

  get currentView() {
    return this.mode === "view" ? this.viewModeTpl.tpl : this.editModeTpl.tpl;
  }

  private viewModeHandler() {
    fromEvent(this.element, "dblclick")
      .pipe(untilDestroyed(this))
      .subscribe(() => {
        this.mode = "edit";
        this.editMode.next(true);
      });
  }

  private editModeHandler() {
    const clickOutside$ = fromEvent(document, "click").pipe(
      filter(
        ({ target }) =>
          document.getElementById("cancelEditing").contains(target as Node) ||
          this.element.contains(target) === false
      ),
      take(1)
    );

    this.editMode$
      .pipe(switchMapTo(clickOutside$), untilDestroyed(this))
      .subscribe((event) => {
        this.update.next();
        this.mode = "view";
      });
  }

  toViewMode() {
    this.update.next();
    this.mode = "view";
  }
}
