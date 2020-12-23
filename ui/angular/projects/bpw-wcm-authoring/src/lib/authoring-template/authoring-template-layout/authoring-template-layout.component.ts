import {
  Component,
  OnInit,
  OnDestroy,
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
  AuthoringTemplate,
  WCM_ACTION_SUCCESSFUL,
  WcmConstants,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";

import { AuthoringTemplateDialog } from "./authoring-template-dialog.component";

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
  selector: "authoring-template-layout",
  templateUrl: "./authoring-template-layout.component.html",
  styleUrls: ["./authoring-template-layout.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class AuthoringTemplateLayoutComponent implements OnInit, OnDestroy {
  @Input() at: AuthoringTemplate;
  builderTargets: string[] = [];
  @Input() controlFields: ControlField[] = [];

  @Input() editing: boolean;

  private baseType: string =
    BASE_AT_TYPE[Math.round(Math.random() * (BASE_AT_TYPE.length - 1))];

  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  blockui: ViewContainerRef;
  componentRef: any;
  blocking: boolean = false;

  constructor(
    private dialog: MatDialog,
    private router: Router,
    private store: Store<fromStore.WcmAppState>,
    private blockUIService: BlockUIService
  ) {}

  ngOnInit() {}
  ngOnDestroy() {}
  droppableItemClass = (item: any) => `wide ${item.inputType.controlType}`;

  getControlField(controlType: string): ControlField {
    return this.controlFields.find(
      (controlField) => controlField.name == controlType
    );
  }

  getIcon(baseType: string) {
    let atTypeIndex = BASE_AT_TYPE.indexOf(baseType);
    let atTypeIcon = "";
    switch (
      atTypeIndex //more_vert
    ) {
      case 0:
        atTypeIcon = "archive";
        break;
      case 1:
        atTypeIcon = "pages";
        break;
      case 2:
        atTypeIcon = "build";
        break;
      case 3:
        atTypeIcon = "file_copy";
        break;
      case 4:
        atTypeIcon = "domain";
        break;
      case 5:
        atTypeIcon = "http";
        break;
      case 6:
        atTypeIcon = "apps";
        break;
      case 7:
      default:
        atTypeIcon = "person_outline";
        break;
    }
    return atTypeIcon;
  }

  editItem() {
    const dialogRef = this.dialog.open(AuthoringTemplateDialog, {
      width: "500px",
      data: this.baseType,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.baseType = result;
      }
    });
    return false;
  }
  /** Predicate function that doesn't allow items to be dropped into a list. */
  noReturnPredicate() {
    return false;
  }

  saveItem() {
    this._createBlockUIComponent(
      this.editing
        ? "Updating authoring template"
        : "Creating authoring template"
    );
    if (this.editing) {
      this.store.dispatch(new fromStore.UpdateAuthoringTemplate(this.at));
    } else {
      this.store.dispatch(new fromStore.CreateAuthoringTemplate(this.at));
    }

    let unsubscribe: Subject<any> = new Subject();
    this.store
      .pipe(
        takeUntil(unsubscribe),
        filter((error) => !!error),
        select(fromStore.getAuthoringTemplateError)
      )
      .subscribe((resp) => {
        unsubscribe.complete();
        if (resp === WCM_ACTION_SUCCESSFUL) {
          this._handleWcmActionStatus(resp);
          setTimeout(() => this._backToAuthoringTemplates(), 500);
        }
      });
  }

  private _backToAuthoringTemplates() {
    this.router.navigate([WcmConstants.NAV_AT_LIST]);
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

  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }
  }
}
