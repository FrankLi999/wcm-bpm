import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewChild,
  ViewContainerRef,
} from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { select, Store } from "@ngrx/store";
import { Observable, Subject } from "rxjs";
import { takeUntil, tap } from "rxjs/operators";

import { wcmAnimations, BlockUIService } from "bpw-common";
import * as fromStore from "bpw-wcm-service";
import { Library, JsonForm, WcmConstants } from "bpw-wcm-service";
import { FormConfig } from "../../config/form-config";

@Component({
  selector: "library",
  templateUrl: "./library.component.html",
  styleUrls: ["./library.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class LibraryComponent implements OnInit, OnDestroy {
  editing: boolean = false;
  formConfig = FormConfig;
  formData: any;
  libraryForm: JsonForm;
  repository: string;
  workspace: string;
  status$: Observable<any>;

  private blocking: boolean = false;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  private blockui: ViewContainerRef;
  private componentRef: any;
  private unsubscribeAll: Subject<any> = new Subject();

  constructor(
    private blockUIService: BlockUIService,
    private store: Store<fromStore.WcmAppState>,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  public ngOnInit(): void {
    this.route.queryParams
      .pipe(tap((param) => this._getParameters(param)))
      .subscribe();

    this.status$ = this.store.pipe(
      select(fromStore.getWcmLibraryStatus),
      takeUntil(this.unsubscribeAll),
      tap((status) => this._handleWcmActionStatus(status))
    );
  }

  public ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
  }

  get successMessage(): string {
    return this.editing
      ? WcmConstants.UI_MESSAGE_UPDATED_LIBRARY
      : WcmConstants.UI_MESSAGE_CREATED_LIBRARY;
  }

  backToItems() {
    this.router.navigate([WcmConstants.NAV_LIBRARY_LIST]);
  }

  upsertItem(formData: any) {
    this._createBlockUIComponent(
      this.editing
        ? WcmConstants.UI_TITLE_UPDATING_LIBRARY
        : WcmConstants.UI_TITLE_CREATING_LIBRARY
    );
    if (this.editing) {
      this.store.dispatch(
        new fromStore.UpdateLibrary(<Library>{
          repository: this.repository,
          workspace: this.workspace,
          name: formData.properties.name,
          title: formData.properties.title,
          description: formData.properties.description,
          language: formData.elements.language,
        })
      );
    } else {
      this.store.dispatch(
        new fromStore.CreateLibrary(<Library>{
          repository: this.repository,
          workspace: this.workspace,
          name: formData.properties.name,
          title: formData.properties.title,
          description: formData.properties.description,
          language: formData.elements.language,
        })
      );
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

  private _getParameters(param: any) {
    this.repository = param.repository;
    this.workspace = param.workspace;
    this.editing = param.editing === "true";
    if (this.editing) {
      this.store
        .pipe(select(fromStore.getWcmLibraries))
        .subscribe((libraries) => this._initializeData(param, libraries));
    } else {
      this.formData = {
        properties: {
          name: "",
          title: "",
          description: "",
        },
        elements: {
          lanaguage: "en",
        },
      };
    }

    this.store
      .pipe(select(fromStore.getAuthoringTemplateForms))
      .subscribe((authoringTemplateForms: { [key: string]: JsonForm[] }) => {
        if (authoringTemplateForms) {
          this.libraryForm = this.editing
            ? authoringTemplateForms[WcmConstants.WCM_LIBRARY_TYPE][1]
            : authoringTemplateForms[WcmConstants.WCM_LIBRARY_TYPE][0];
        }
      });
  }

  private _initializeData(param: any, libraries: Library[]) {
    let library = libraries[param.libraryIndex];
    this.formData = {
      properties: {
        name: library.name,
        title: library.title,
        description: library.description,
      },
      elements: {
        language: library.language,
      },
    };
  }

  private _handleWcmActionStatus(status: any) {
    console.log(
      "<<<<<<<<<<<<< _handleWcmActionStatus xxxx, ",
      status,
      this.blocking
    );
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }
  }
}
