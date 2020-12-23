import {
  Component,
  OnInit,
  OnDestroy,
  Input,
  ElementRef,
  ViewChild,
} from "@angular/core";
import { Subscription, Observable, of } from "rxjs";
import { switchMap, map } from "rxjs/operators";
import { select, Store } from "@ngrx/store";
import cloneDeep from "lodash/cloneDeep";

import * as fromStore from "bpw-wcm-service";
import { JsonForm, ContentItem, ContentItemService } from "bpw-wcm-service";
import { FormConfig } from "../../config/form-config";

@Component({
  selector: "in-place-content-item-editor",
  templateUrl: "./in-place-content-item-editor.component.html",
  styleUrls: ["./in-place-content-item-editor.component.scss"],
})
export class InPlaceContentItemEditorComponent implements OnInit, OnDestroy {
  @Input() repository: string;
  @Input() workspace: string;
  @Input() wcmPath: string;
  authoringTemplate: string;
  contentItem: ContentItem;
  contentItemData: any = {};
  sub: Subscription;
  jsonForm: JsonForm;
  formConfig = FormConfig;
  @ViewChild("cancelEditing") cancelEditing: ElementRef;

  constructor(
    private contentItemService: ContentItemService,
    private store: Store<fromStore.WcmAppState>
  ) {}

  ngOnInit() {
    this.sub = this._getItem()
      .pipe(
        switchMap((item) => {
          this.contentItem = item;
          this.authoringTemplate = item.authoringTemplate;
          return this._getAuthoringTemplateForms();
        })
      )
      .subscribe((jsonForms) => (this.jsonForm = jsonForms[1]));
  }

  private _getItem(): Observable<ContentItem> {
    return this.contentItemService.getContentItem(
      this.repository,
      this.workspace,
      this.wcmPath
    );
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

  saveItem(formData: any) {
    // editing;
    const contentItem: ContentItem = {
      id: this.contentItem.id,
      elements: { ...formData.elements },
      properties: {
        authoringTemplate: this.contentItem.authoringTemplate,
        ...formData.properties,
      },
      repository: this.repository,
      locked: this.contentItem.locked,
      checkedOut: this.contentItem.checkedOut,
      wcmPath: this.wcmPath,
      workspace: this.workspace,
    };

    this.contentItemService
      .updateContentItem(contentItem)
      .subscribe((event: any) => {
        console.log(event);
      });
  }

  private _getContentItemData(contentItem: ContentItem) {
    return {
      properties: cloneDeep(contentItem.properties),
      elements: cloneDeep(contentItem.elements),
    };
  }
}
