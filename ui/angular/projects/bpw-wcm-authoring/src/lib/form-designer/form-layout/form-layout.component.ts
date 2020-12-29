import {
  Component,
  OnInit,
  Input,
  ViewEncapsulation,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import { Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { Subject } from "rxjs";
import { filter, takeUntil } from "rxjs/operators";

import { Store, select } from "@ngrx/store";

import { BlockUIService } from "bpw-common";
import {
  ControlField,
  Form,
  WCM_ACTION_SUCCESSFUL,
  WcmConstants,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";

import { FormLayoutDialog } from "./form-layout-dialog.component";

const BASE_AT_TYPE: string[] = [
  "Content",
  "Page",
  "Widget",
  "File",
  "Key/Value",
  "VanityURL",
  "Form",
  "Persona",
];

@Component({
  selector: "form-layout",
  templateUrl: "./form-layout.component.html",
  styleUrls: ["./form-layout.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class FormLayoutComponent implements OnInit {
  builderTargets: string[] = [];
  @Input() controlFields: ControlField[] = [];
  @Input() editing: boolean;
  @Input() form: Form;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;

  constructor(
    private dialog: MatDialog,
    private router: Router,
    private store: Store<fromStore.WcmAppState>,
    private blockUIService: BlockUIService
  ) {}

  ngOnInit() {}

  /** Predicate function that doesn't allow items to be dropped into a list. */
  noReturnPredicate() {
    return false;
  }

  droppableItemClass = (item: any) => `wide ${item.inputType.controlType}`;

  getControlField(controlType: string): ControlField {
    return this.controlFields.find(
      (controlField) => controlField.name == controlType
    );
  }

  getIcon(baseType: string) {
    let atIndex = BASE_AT_TYPE.indexOf(baseType);
    let atIcon = "";
    switch (
      atIndex //more_vert
    ) {
      case 0:
        atIcon = "archive";
        break;
      case 1:
        atIcon = "pages";
        break;
      case 2:
        atIcon = "build";
        break;
      case 3:
        atIcon = "file_copy";
        break;
      case 4:
        atIcon = "domain";
        break;
      case 5:
        atIcon = "http";
        break;
      case 6:
        atIcon = "apps";
        break;
      case 7:
      default:
        atIcon = "person_outline";
        break;
    }
    return atIcon;
  }

  editForm() {
    const dialogRef = this.dialog.open(FormLayoutDialog, {
      width: "500px",
      data: this.form,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.form = result;
      }
    });
    return false;
  }

  saveForm() {
    this._createBlockUIComponent(
      this.editing ? "Updating form" : "Creating form"
    );
    let unsubscribe: Subject<any> = new Subject();
    this.store
      .pipe(
        takeUntil(unsubscribe),
        filter((error) => !!error),
        select(fromStore.getFormError)
      )
      .subscribe((resp) => {
        unsubscribe.complete();
        if (resp === WCM_ACTION_SUCCESSFUL) {
          this._handleWcmActionStatus(resp);
          setTimeout(() => this._backToItems(), 500);
        }
      });
    if (this.editing) {
      this.store.dispatch(new fromStore.UpdateForm(this.form));
    } else {
      this.store.dispatch(new fromStore.CreateForm(this.form));
    }
  }

  private _backToItems() {
    this.router.navigate([WcmConstants.NAV_FORM_LIST]);
  }

  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }
  }

  private _createBlockUIComponent(message: string) {
    this.componentRef = this.blockUIService.createBlockUIComponent(
      message,
      this.blockui
    );
    this.blocking = true;
  }

  private _destroyBlockUIComponent() {
    this.blockUIService.destroyBlockUIComponent(
      this.blockui,
      this.componentRef
    );
    this.blocking = false;
  }
}
