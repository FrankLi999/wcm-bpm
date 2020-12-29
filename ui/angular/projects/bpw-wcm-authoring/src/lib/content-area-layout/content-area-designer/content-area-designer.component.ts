import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  Input,
} from "@angular/core";
import cloneDeep from "lodash/cloneDeep";
import { select, Store } from "@ngrx/store";
import { ActivatedRoute, Router } from "@angular/router";
import { wcmAnimations } from "bpw-common";
import { Subscription, Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";

import {
  CreateNewContentAreaLayout,
  WcmAppState,
  ContentAreaLayout,
  EditContentAreaLayout,
  WcmConstants,
  WcmUtils,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";

@Component({
  selector: "content-area-designer",
  templateUrl: "./content-area-designer.component.html",
  styleUrls: ["./content-area-designer.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class ContentAreaDesignerComponent implements OnInit, OnDestroy {
  @Input() layoutName: string;
  @Input() editing: boolean = false;
  @Input() repository: string;
  @Input() workspace: string;
  @Input() library: string;

  private sub: Subscription;
  private unsubscribeAll: Subject<any>;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private store: Store<WcmAppState>
  ) {
    this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.sub = this.route.queryParams.subscribe((param) => {
      this.repository = param.repository;
      this.workspace = param.workspace;
      this.library = param.library;
      this.layoutName = param.layoutName;
      this.editing = param.editing === "true";
      if (this.editing) {
        this.store
          .pipe(
            select(fromStore.getContentAreaLayouts),
            takeUntil(this.unsubscribeAll)
          )
          .subscribe(
            (contentAreaLayouts: { [key: string]: ContentAreaLayout }) => {
              if (contentAreaLayouts) {
                const layout: ContentAreaLayout = cloneDeep(
                  contentAreaLayouts[
                    WcmUtils.contentAreaLayoutPath(
                      this.library,
                      this.layoutName
                    )
                  ]
                );
                this.store.dispatch(new EditContentAreaLayout(layout));
              }
            }
          );
      } else {
        this.store.dispatch(
          new CreateNewContentAreaLayout(
            this.repository,
            this.workspace,
            this.library
          )
        );
      }
    });
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.sub.unsubscribe();
    this.unsubscribeAll.complete();
  }

  backToItems() {
    this.router.navigate([WcmConstants.NAV_LAYOUT_LIST]);
  }
}
