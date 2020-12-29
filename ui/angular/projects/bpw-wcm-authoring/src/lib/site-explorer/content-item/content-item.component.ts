import { Component, OnInit, OnDestroy, Input } from "@angular/core";
import { Router } from "@angular/router";
import { ActivatedRoute } from "@angular/router";
import { Subscription, Observable, of } from "rxjs";
import { switchMap, map } from "rxjs/operators";
import { select, Store } from "@ngrx/store";
import cloneDeep from "lodash/cloneDeep";

import * as fromStore from "bpw-wcm-service";
import {
  JsonForm,
  ContentItem,
  ContentItemService,
  WcmConstants,
} from "bpw-wcm-service";
import { FormConfig } from "../../config/form-config";
import { PerfectScrollconfig } from "../../config/wcm-authoring.config";

@Component({
  selector: "content-item",
  templateUrl: "./content-item.component.html",
  styleUrls: ["./content-item.component.scss"],
})
export class ContentItemComponent implements OnInit, OnDestroy {
  authoringTemplate: string;
  contentItem: ContentItem;
  contentItemData: any = {};
  editing: boolean = false;
  reviewing: boolean = false;
  formConfig = FormConfig;
  formIsValid: boolean;
  itemForm: JsonForm;
  liveFormData: any;
  perfectScrollconfig = PerfectScrollconfig;
  repository: string;
  workspace: string;
  wcmPath: string;

  private sub: Subscription;
  constructor(
    private contentItemService: ContentItemService,
    private route: ActivatedRoute,
    private router: Router,
    private store: Store<fromStore.WcmAppState>
  ) {}

  ngOnInit() {
    this.sub = this.route.queryParams
      .pipe(
        switchMap((param) => this._handleParams(param)),
        switchMap((item) => {
          this.contentItem = item;
          return this._getAuthoringTemplateForms();
        })
      )
      .subscribe(
        (authoringTemplateForms) =>
          (this.itemForm = this.editing
            ? authoringTemplateForms[1]
            : authoringTemplateForms[0])
      );
  }

  get defaultWorkspace(): boolean {
    return this.workspace === WcmConstants.WS_DEFAULT;
  }

  get draftWorkspace(): boolean {
    return this.workspace === WcmConstants.WS_DRAFT;
  }

  approveDraft() {
    this.router.navigate(
      ["/wcm-authoring/approve-draft"],
      this._draftTaskParams()
    );
  }

  backToItems() {
    this.router.navigate(["/wcm-authoring/site-explorer/navigator"]);
  }

  isFormDataValid(isValid: boolean) {
    this.formIsValid = isValid;
  }

  onFormDataChanges(data: any) {
    this.liveFormData = data;
  }

  previewDraft() {
    console.log(">>>>>>>>>>>> preview draft");
  }

  saveDraft() {
    const contentItem: ContentItem = {
      id: this.contentItem.id,
      authoringTemplate: this.contentItem.authoringTemplate,
      elements: { ...this.liveFormData.elements },
      properties: {
        ...this.liveFormData.properties,
      },
      repository: this.repository,
      locked: this.contentItem.locked,
      checkedOut: this.contentItem.checkedOut,
      wcmPath: this.wcmPath,
      workspace: this.workspace,
      workflow: {
        ...this.contentItem.workflow,
      },
    };

    this.contentItemService.updateDraft(contentItem).subscribe((event: any) => {
      console.log(event);
    });
  }
  publishItem() {
    const contentItem: ContentItem = {
      id: this.contentItem.id,
      authoringTemplate: this.contentItem.authoringTemplate,
      elements: { ...this.liveFormData.elements },
      properties: {
        ...this.liveFormData.properties,
      },
      repository: this.repository,
      locked: this.contentItem.locked,
      checkedOut: this.contentItem.checkedOut,
      wcmPath: this.wcmPath,
      workspace: this.workspace,
    };

    if (this.editing) {
      this.contentItemService
        .updateContentItem(contentItem)
        .subscribe((event: any) => {
          console.log(event);
        });
    } else {
      this.contentItemService
        .createAndPublishContentItem(contentItem)
        .subscribe((event: any) => {
          console.log(event);
        });
    }
  }

  rejectDraft() {
    this.router.navigate(
      ["/wcm-authoring/reject-draft"],
      this._draftTaskParams()
    );
  }

  saveItemAsDraft() {
    const contentItem: ContentItem = {
      id: this.contentItem.id,
      authoringTemplate: this.contentItem.authoringTemplate,
      elements: { ...this.liveFormData.elements },
      properties: {
        ...this.liveFormData.properties,
      },
      repository: this.repository,
      locked: this.contentItem.locked,
      checkedOut: this.contentItem.checkedOut,
      wcmPath: this.wcmPath,
      workspace: this.workspace,
    };

    if (this.editing) {
      this.contentItemService
        .updateDraft(contentItem)
        .subscribe((event: any) => {
          console.log(event);
        });
    } else {
      this.contentItemService
        .createDraft(contentItem)
        .subscribe((event: any) => {
          console.log(event);
        });
    }
  }

  private _draftTaskParams() {
    return {
      queryParams: {
        contentId: this.contentItem.id,
        wcmPath: this.contentItem.wcmPath,
        repository: this.contentItem.repository,
        workspace: this.contentItem.workspace,
        processInstanceId: this.contentItem.workflow?.processInstanceId,
        reviewer: this.contentItem.workflow?.reviewer,
      },
    };
  }
  private _handleParams(param: any): Observable<ContentItem> {
    this.wcmPath = param.wcmPath;
    this.workspace = param.workspace;
    this.repository = param.repository;
    this.authoringTemplate = param.authoringTemplate;
    this.editing = param.editing === "true";
    this.reviewing = param.reviewing === "true";
    return this._getItem();
  }

  private _getItem(): Observable<ContentItem> {
    if (this.editing || this.reviewing) {
      return this.contentItemService.getContentItem(
        this.repository,
        this.workspace,
        this.wcmPath
      );
    } else {
      return of({
        id: "",
        repository: this.repository,
        workspace: this.workspace,
        wcmPath: this.wcmPath,
        locked: false,
        lifeCycleStage: "darft",
        checkedOut: false,
        authoringTemplate: this.authoringTemplate,
        properties: {
          workflow: "bpmn:wcm_content_flow",
        },
        elements: {},
      });
    }
  }

  private _getAuthoringTemplateForms(): Observable<JsonForm[]> {
    this.contentItemData = this._getContentItemData(this.contentItem);
    return this.store.pipe(
      select(fromStore.getAuthoringTemplateForms),
      map((authoringTemplateForms) => {
        return authoringTemplateForms[this.contentItem.authoringTemplate];
      })
    );
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  createContent(formData: any) {
    console.log(">>>>>>>> submit button will be removed");
  }

  private _getContentItemData(contentItem: ContentItem) {
    return {
      properties: cloneDeep(contentItem.properties),
      elements: cloneDeep(contentItem.elements),
    };
  }
}
