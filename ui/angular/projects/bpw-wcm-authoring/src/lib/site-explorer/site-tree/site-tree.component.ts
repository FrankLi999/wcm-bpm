import {
  Component,
  OnInit,
  OnDestroy,
  ViewEncapsulation,
  ViewContainerRef,
  ViewChild
} from "@angular/core";
import { Router } from "@angular/router";
import { takeUntil } from "rxjs/operators";
import { select, Store } from "@ngrx/store";
import { MatDialog } from "@angular/material/dialog";
import { Observable } from "rxjs";
import { filter, tap } from "rxjs/operators";

import {
  wcmAnimations,
  ConfirmationDialogService,
  BlockUIService,
} from "bpw-common";
import {
  WcmOperation,
  JsonForm,
  WcmService,
  SiteAreaService,
  WcmConstants,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { SidebarService } from "bpw-common";
import { WcmNavigatorComponent } from "../../components/wcm-navigator/wcm-navigator.component";

import { UploadZipfileDialogComponent } from "../../dialog/upload-zipfile-dialog/upload-zipfile-dialog.component";
import { NewFolderDialogComponent } from "../../dialog/new-folder-dialog/new-folder-dialog.component";
import { NewThemeDialogComponent } from "../../dialog/new-theme-dialog/new-theme-dialog.component";
import { SelectAuthoringTemplateDialogComponent } from "../select-authoring-template-dialog/select-authoring-template-dialog.component";

@Component({
  selector: "site-tree",
  templateUrl: "./site-tree.component.html",
  styleUrls: ["./site-tree.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class SiteTreeComponent extends WcmNavigatorComponent
  implements OnInit, OnDestroy {
  functionMap: { [key: string]: Function } = {};
  authoringTemplateFormMap: { [key: string]: JsonForm[] } = {};
  status$: Observable<any>;
  @ViewChild("blockUIContainer", { read: ViewContainerRef })
  blockui: ViewContainerRef;
  blockuiComponentRef: any;
  componentRef: any;
  blocking: boolean = false;
  constructor(
    protected matDialog: MatDialog,
    protected sidebarService: SidebarService,
    protected store: Store<fromStore.WcmAppState>,
    protected wcmService: WcmService,
    private siteAreaService: SiteAreaService,
    private blockUIService: BlockUIService,
    private confirmationDialogService: ConfirmationDialogService,
    private router: Router
  ) {
    super(matDialog, sidebarService, store, wcmService);
  }

  ngOnInit() {
    this.rootNode = "rootSiteArea";
    this.rootNodeType = "bpw:system_siteAreaType";
    this.functionMap["Upload.file"] = this.uploadZipFile;
    this.functionMap["Create.folder"] = this.createFolder;
    this.functionMap["Create.theme"] = this.createTheme;
    // this.functionMap["Remove.folder"] = this.removeItem;
    // this.functionMap["Remove.file"] = this.removeItem;
    // this.functionMap["Delete.theme"] = this.removeItem;

    this.functionMap["Create.siteArea"] = this.createSiteArea;
    this.functionMap["Edit.siteArea"] = this.editSiteArea;
    this.functionMap["Preview.siteArea"] = this.previewSiteArea;
    this.functionMap["Purge.siteArea"] = this.removeSiteArea;
    this.functionMap["Create.content"] = this.createContent;
    // this.functionMap["Edit.content"] = this.editContent;
    // this.functionMap["Purge.content"] = this.removeItem;

    this.nodeFilter = {
      wcmPath: "",
      nodeTypes: ["bpw:system_siteAreaType", "bpw:folder"],
    };
    this.store
      .pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getAuthoringTemplateForms)
      )
      .subscribe((authoringTemplateForms: { [key: string]: JsonForm[] }) => {
        if (authoringTemplateForms) {
          this.authoringTemplateFormMap = authoringTemplateForms;
        }
      });
    this.store
      .pipe(takeUntil(this.unsubscribeAll), select(fromStore.getOperations))
      .subscribe((operations: { [key: string]: WcmOperation[] }) => {
        if (operations) {
          this.operationMap = operations;
        }
      });
    this.status$ = this.store.pipe(
      select(fromStore.getAuthoringTemplateError),
      filter((status) => !!status),
      tap((status) => this._handleWcmActionStatus(status))
    );
    super.ngOnInit();
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.loadError && this.store.dispatch(new fromStore.WcmSystemClearError());
  }

  doNodeOperation(item: String, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(
      this
    );
  }

  uploadZipFile() {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(UploadZipfileDialogComponent, {
      panelClass: "zipfile-upload-dialog",
      data: {
        wcmPath: node.data.wcmPath,
        repositoryName: node.data.repository,
        workspaceName: node.data.workspace,
      },
    });
    dialogRef.afterClosed().subscribe((response) => {
      this.load(node);
    });
  }

  createFolder() {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(NewFolderDialogComponent, {
      panelClass: "folder-new-dialog",
      data: {
        jsonFormObject: this.authoringTemplateFormMap[
          WcmConstants.WCM_FOLDER_TYPE
        ][0].formSchema,
        wcmPath: node.data.wcmPath,
        repositoryName: node.data.repository,
        workspaceName: node.data.workspace,
      },
    });
    dialogRef.afterClosed().subscribe((response) => {
      this.load(node);
    });
  }

  createTheme() {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(NewThemeDialogComponent, {
      panelClass: "theme-new-dialog",
      data: {
        jsonFormObject: this.authoringTemplateFormMap[
          "bpwizard/default/system/themeType"
        ][0].formSchema,
        wcmPath: node.data.wcmPath,
        repositoryName: node.data.repository,
        workspaceName: node.data.workspace,
      },
    });
    dialogRef.afterClosed().subscribe((response) => {
      this.load(node);
    });
  }

  // removeItem() {
  //   const node = this.activeNode;
  //   const confirmDialogRef = this.confirmationDialogService.confirm(
  //     "Are you sure you want to delete the content area layout?"
  //   );

  //   confirmDialogRef.afterClosed().subscribe((result) => {
  //     if (result) {
  //       this.blockuiComponentRef = this._createBlockUIComponent(
  //         "Deleting content area layout"
  //       );
  //       this.confirmationDialogService.closeConfirmation();
  //       this.wcmService
  //         .purgeWcmItem(
  //           node.data.repository,
  //           node.data.workspace,
  //           node.data.wcmPath
  //         )
  //         .subscribe(
  //           (event: any) => {
  //             if (event.type === 4) {
  //               this.nodeRemoved(node);
  //             }
  //           },
  //           (response) => {
  //             console.log("removeFolder call in error", response);
  //             console.log(response);
  //           },
  //           () => {
  //             console.log("removeFolder observable is now completed.");
  //           }
  //         );

  //       // }, 3500);
  //     }
  //   });
  // }

  removeSiteArea() {
    const node = this.activeNode;
    const confirmDialogRef = this.confirmationDialogService.confirm(
      "Are you sure you want to delete the content area layout?"
    );

    confirmDialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.blockuiComponentRef = this._createBlockUIComponent(
          "Deleting content area layout"
        );
        this.confirmationDialogService.closeConfirmation();
        this.siteAreaService
          .purgeSiteArea(
            node.data.repository,
            node.data.workspace,
            node.data.wcmPath
          )
          .subscribe(
            (event: any) => {
              if (event.type === 4) {
                this.nodeRemoved(node);
              }
            },
            (response) => {
              console.log("removeFolder call in error", response);
              console.log(response);
            },
            () => {
              console.log("removeFolder observable is now completed.");
            }
          );

        // }, 3500);
      }
    });
  }

  previewSiteArea() {
    const node = this.activeNode;
    this.router.navigate([WcmConstants.NAV_PREVIEW], {
      queryParams: {
        repository: node.data.repository,
        workspace: node.data.workspace,
        wcmPath: node.data.wcmPath,
      },
    });
  }

  createSiteArea() {
    const node = this.activeNode;
    this.router.navigate([WcmConstants.NAV_SA_NEW_SA], {
      queryParams: {
        wcmPath: node.data.wcmPath,
        repository: node.data.repository,
        workspace: node.data.workspace,
        editing: false,
      },
    });
  }

  editSiteArea() {
    const node = this.activeNode;
    this.router.navigate(["/wcm-authoring/site-explorer/edit-sa"], {
      queryParams: {
        wcmPath: node.data.wcmPath,
        repository: node.data.repository,
        workspace: node.data.workspace,
        editing: true,
      },
    });
  }

  createContent() {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(
      SelectAuthoringTemplateDialogComponent,
      {
        panelClass: "content-new-dialog",
      }
    );
    dialogRef.afterClosed().subscribe((response) => {
      console.log(response);
      if (response && response.selectedAuthoringTemplate) {
        this.router.navigate(["/wcm-authoring/site-explorer/new-content"], {
          queryParams: {
            authoringTemplate: response.selectedAuthoringTemplate,
            wcmPath: node.data.wcmPath,
            repository: node.data.repository,
            workspace: node.data.workspace,
            editing: false,
          },
        });
      }
    });
  }

  // editContent() {
  //   const node = this.activeNode;
  //   this.contentItemService
  //     .getContentItem(
  //       node.data.repository,
  //       node.data.workspace,
  //       node.data.wcmPath
  //     )
  //     .subscribe((contentItem) => {
  //       console.log("editCurrentNode contentItem", contentItem);
  //       this.router.navigate(["/wcm-authoring/site-explorer/edit-content"], {
  //         queryParams: {
  //           authoringTemplate: contentItem.authoringTemplate,
  //           wcmPath: node.data.wcmPath,
  //           repository: node.data.repository,
  //           workspace: node.data.workspace,
  //           editing: true,
  //         },
  //       });
  //     });
  // }

  private _createBlockUIComponent(message: string) {
    this.componentRef = this.blockUIService.createBlockUIComponent(
      message,
      this.blockui
    );
    this.blocking = true;
  }

  private _handleWcmActionStatus(status: any) {
    if (status != null && this.blocking) {
      this._destroyBlockUIComponent();
    }
  }

  private _destroyBlockUIComponent() {
    this.blockUIService.destroyBlockUIComponent(
      this.blockui,
      this.componentRef
    );
    this.blocking = false;
  }
}
