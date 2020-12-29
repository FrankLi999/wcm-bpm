import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  ViewEncapsulation,
} from "@angular/core";
import { Router } from "@angular/router";
import { FormControl } from "@angular/forms";
import { FlatTreeControl } from "@angular/cdk/tree";
import {
  MatTreeFlatDataSource,
  MatTreeFlattener,
} from "@angular/material/tree";
import { BehaviorSubject, Observable, Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { MatMenuTrigger } from "@angular/material/menu";
import { MatDialog } from "@angular/material/dialog";
import { select, Store } from "@ngrx/store";

import { wcmAnimations, SidebarService } from "bpw-common";
import {
  WcmRepository,
  WcmWorkspace,
  RestNode,
  WcmOperation,
  JsonForm,
  WcmAppState,
  ModeshapeService,
  getGetWcmSystemError,
  getWcmRepositories,
  getAuthoringTemplateForms,
  WcmSystemClearError,
  getOperations,
  WcmConstants,
  WcmUtils,
} from "bpw-wcm-service";
import { WcmConfigService } from "bpw-wcm-service";

import { UploadZipfileDialogComponent } from "../../dialog/upload-zipfile-dialog/upload-zipfile-dialog.component";
import { NewFolderDialogComponent } from "../../dialog/new-folder-dialog/new-folder-dialog.component";
import { NewThemeDialogComponent } from "../../dialog/new-theme-dialog/new-theme-dialog.component";
import { NewSiteConfigDialogComponent } from "../../dialog/new-site-config-dialog/new-site-config-dialog.component";
import { NewContentDialogComponent } from "../../dialog/new-content-dialog/new-content-dialog.component";

/** Nested node */
class JcrNode {
  childrenChange = new BehaviorSubject<JcrNode[]>([]);
  get children(): JcrNode[] {
    return this.childrenChange.value;
  }

  constructor(
    public id: string,
    public name: string,
    public value: WcmRepository | WcmWorkspace | RestNode,
    public parent: JcrNode | null
  ) {}
}

/** Flat node with expandable and level information */
class JcrFlatNode {
  constructor(
    public id: string,
    public name: string,
    public value: WcmRepository | WcmWorkspace | RestNode,
    public level = 1,
    public expandable = false,
    public active = false
  ) {}
}

@Component({
  selector: "jcr-explorer",
  templateUrl: "./jcr-explorer.component.html",
  styleUrls: ["./jcr-explorer.component.scss"],
  encapsulation: ViewEncapsulation.None,
  animations: wcmAnimations,
})
export class JcrExplorerComponent implements OnInit, OnDestroy {
  functionMap: { [key: string]: Function } = {};
  // jcrNodeMap = new Map<string, JcrNode>();
  jcrNodeMap: { [key: string]: JcrNode } = {};
  operationMap: { [key: string]: WcmOperation[] } = {};
  authoringTemplateFormMap: { [key: string]: JsonForm[] } = {};
  currentNodeOperations: WcmOperation[];
  activeNode: JcrFlatNode = null;
  nodeMap: { [key: string]: JcrFlatNode } = {};
  treeControl: FlatTreeControl<JcrFlatNode>;
  treeFlattener: MatTreeFlattener<JcrNode, JcrFlatNode>;
  // Flat tree data source
  dataSource: MatTreeFlatDataSource<JcrNode, JcrFlatNode>;
  dataChange = new BehaviorSubject<JcrNode[]>([]);
  loadError: string;
  @ViewChild("jcrExplorer", { static: true }) tree;
  @ViewChild("contextMenu", { static: true }) contextMenu: MatMenuTrigger;
  // Private
  private unsubscribeAll: Subject<any>;
  // private websocketService: ContentWebSocketService;
  jcrMessage = "";
  searchInput: FormControl;
  constructor(
    protected wcmConfigService: WcmConfigService,
    private sidebarService: SidebarService,
    private modeshapeService: ModeshapeService,
    private store: Store<WcmAppState>,
    private router: Router,
    private matDialog: MatDialog
  ) {
    // Set the defaults
    this.searchInput = new FormControl("");
    this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    // this.websocketService = new ContentWebSocketService(this);
    this.functionMap["Upload.file"] = this.uploadZipFile;
    this.functionMap["Create.folder"] = this.createFolder;
    this.functionMap["Create.theme"] = this.createTheme;
    this.functionMap["Remove.folder"] = this.removeItem;
    this.functionMap["Remove.file"] = this.removeItem;
    this.functionMap["Delete.theme"] = this.removeItem;
    this.functionMap["Delete.renderTemplate"] = this.removeItem;

    this.functionMap["Create.siteArea"] = this.createSiteArea;
    this.functionMap["Edit.siteArea"] = this.editSiteArea;
    this.functionMap["Preview.siteArea"] = this.previewSiteArea;
    this.functionMap["Delete.siteArea"] = this.removeItem;
    this.functionMap["Create.content"] = this.createContent;
    this.functionMap["Delete.content"] = this.removeItem;
    this.functionMap["Create.siteConfig"] = this.createSiteConfig;
    this.functionMap["Delete.siteConfig"] = this.removeSiteConfig;

    this.treeFlattener = new MatTreeFlattener(
      this.transformer,
      this.getLevel,
      this.isExpandable,
      this.getChildren
    );

    this.treeControl = new FlatTreeControl<JcrFlatNode>(
      this.getLevel,
      this.isExpandable
    );

    this.dataSource = new MatTreeFlatDataSource(
      this.treeControl,
      this.treeFlattener
    );

    this.dataChange.subscribe((data) => {
      this.dataSource.data = data;
      this.resetCurrentOperations();
    });
    this.store
      .pipe(select(getGetWcmSystemError), takeUntil(this.unsubscribeAll))
      .subscribe((loadError: string) => {
        if (loadError) {
          this.loadError = loadError;
        }
      });
    this.store
      .pipe(select(getWcmRepositories), takeUntil(this.unsubscribeAll))
      .subscribe(
        (repositories: WcmRepository[]) => {
          if (repositories) {
            const repoNodes = repositories.map((repository) =>
              this.generateRepositoryNode(repository.name, repository)
            );
            this.dataChange.next(repoNodes);
            if (this.activeNode != null) {
              this.activeNode.active = true;
              this.treeControl.expand(this.activeNode);
            }
          }
        },
        (response) => {
          console.log("Get Repository call ended in error", response);
          console.log(response);
        },
        () => {
          console.log("The Get Repository observable is now completed.");
        }
      );

    this.store
      .pipe(takeUntil(this.unsubscribeAll), select(getAuthoringTemplateForms))
      .subscribe(
        (atForms: { [key: string]: JsonForm[] }) => {
          if (atForms) {
            this.authoringTemplateFormMap = atForms;
          }
        },
        (response) => {
          console.log(
            "getAuthoringTemplateAsJsonSchema call ended in error",
            response
          );
          console.log(response);
        },
        () => {
          console.log(
            "getAuthoringTemplateAsJsonSchema observable is now completed."
          );
        }
      );
    // this.websocketService._connect();
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.loadError && this.store.dispatch(new WcmSystemClearError());
    // this.websocketService._disconnect();
  }

  ngAfterViewInit() {
    //console.log(this.modeshapeService);
    this.treeControl.expand(this.activeNode);
  }

  handleJcrMessage(jcrMessage) {
    this.jcrMessage = jcrMessage;
  }

  /**
   * Toggle the sidebar
   *
   * @param name
   */
  toggleSidebar(name): void {
    this.sidebarService.getSidebar(name).toggleOpen();
  }

  private generateRepositoryNode(
    name: string,
    repository: WcmRepository
  ): JcrNode {
    const repoId = `repo-${name}`;
    let jcrNode: JcrNode = this.jcrNodeMap[repoId];
    if (!jcrNode) {
      jcrNode = new JcrNode(repoId, name, repository, null);
      const workspaceNodes = repository.workspaces.map(
        (workspace: WcmWorkspace) =>
          this.generateWorkspaceNode(
            jcrNode,
            workspace.name,
            repository.name,
            workspace
          )
      );

      this.jcrNodeMap[jcrNode.id] = jcrNode;
      jcrNode.childrenChange.next(workspaceNodes);
    }
    return jcrNode;
  }

  private generateWorkspaceNode(
    repository: JcrNode,
    workspaceName: string,
    repositoryName: string,
    workspace: WcmWorkspace
  ): JcrNode {
    const wsId = `ws-${workspaceName}`;
    if (this.jcrNodeMap[wsId]) {
      return this.jcrNodeMap[wsId];
    }

    // workspace.repositoryName = repositoryName;
    const workspaceNode = new JcrNode(
      wsId,
      workspaceName,
      workspace,
      repository
    );
    this.jcrNodeMap[workspaceNode.id] = workspaceNode;
    this.store
      .pipe(takeUntil(this.unsubscribeAll), select(getOperations))
      .subscribe(
        (operations: { [key: string]: WcmOperation[] }) => {
          if (operations) {
            this.operationMap = operations;
          }
        },
        (response) => {
          console.log("GET call in error", response);
          console.log(response);
        },
        () => {
          console.log("The GET observable is now completed.");
        }
      );
    return workspaceNode;
  }

  private generateJcrNodeNode(
    parent: JcrNode,
    jcrNodeName: string,
    repository: string,
    workspace: string,
    wcmPath: string,
    restNode: RestNode
  ): JcrNode {
    wcmPath = wcmPath ? `${wcmPath}/${jcrNodeName}` : `/${jcrNodeName}`;
    const nodeId = `jcr-${wcmPath}`;
    if (this.jcrNodeMap[nodeId]) {
      return this.jcrNodeMap[nodeId];
    }
    restNode.repository = repository;
    restNode.workspace = workspace;
    restNode.wcmPath = wcmPath;
    const jcrNodeNode = new JcrNode(nodeId, jcrNodeName, restNode, parent);
    this.jcrNodeMap[jcrNodeNode.id] = jcrNodeNode;
    return jcrNodeNode;
  }

  getChildren = (node: JcrNode): Observable<JcrNode[]> => node.childrenChange;
  transformer = (node: JcrNode, level: number) => {
    let jcrFlatNode = this.nodeMap[node.id];
    if (!jcrFlatNode) {
      jcrFlatNode = new JcrFlatNode(
        node.id,
        node.name,
        node.value,
        level,
        node.children.length > 0,
        false
      );
      this.nodeMap[node.id] = jcrFlatNode;
    } else {
      jcrFlatNode.expandable = node.children.length > 0;
    }

    if (level == 0 && this.activeNode == undefined) {
      this.activeNode = jcrFlatNode;
    }
    return jcrFlatNode;
  };

  getLevel = (node: JcrFlatNode) => node.level;

  isExpandable = (node: JcrFlatNode) => {
    return node.expandable;
  };

  hasChild = (_: number, _nodeData: JcrFlatNode) => {
    return _nodeData.expandable;
  };

  isRestNode(node: JcrFlatNode): boolean {
    return node ? node.id.startsWith("jcr-") : false;
  }

  isRepository(node: JcrFlatNode): boolean {
    return node ? node.id.startsWith("repo-") : false;
  }

  isWorkspace(node: JcrFlatNode): boolean {
    return node ? node.id.startsWith("ws-") : false;
  }

  loadRestNode(parent: JcrNode, onlyFirstTime = false) {
    const repository = (parent.value as RestNode).repository;
    const workspace = (parent.value as RestNode).workspace;
    const wcmPath = (parent.value as RestNode).wcmPath;
    this.modeshapeService.getItems(repository, workspace, wcmPath, 2).subscribe(
      (restNode: RestNode) => {
        if (restNode && restNode.children) {
          (parent.value as RestNode).customProperties =
            restNode.customProperties;
          (parent.value as RestNode).jcrProperties = restNode.jcrProperties;
          const restNodes = restNode.children.map((jcrNode) =>
            this.generateJcrNodeNode(
              parent,
              jcrNode.name,
              repository,
              workspace,
              wcmPath,
              jcrNode
            )
          );
          parent.childrenChange.next(restNodes);
          this.dataChange.next(this.dataChange.value);
        }
      },
      (response) => {
        console.log("GET call in error", response);
        console.log(response);
      },
      () => {
        console.log("The GET observable is now completed.");
      }
    );
  }

  loadWorkspace(parent: JcrNode, onlyFirstTime = false) {
    const repository = (parent.parent.value as WcmRepository).name;
    const workspace = (parent.value as WcmWorkspace).name;
    const wcmPath = "";
    this.modeshapeService.getItems(repository, workspace, wcmPath).subscribe(
      (restNode: RestNode) => {
        if (restNode && restNode.children) {
          const restNodes = restNode.children.map((restNode) =>
            this.generateJcrNodeNode(
              parent,
              restNode.name,
              repository,
              workspace,
              wcmPath,
              restNode
            )
          );
          parent.childrenChange.next(restNodes);
          this.dataChange.next(this.dataChange.value);
        }
      },
      (response) => {
        console.log("GET call in error", response);
        console.log(response);
      },
      () => {
        console.log("The GET observable is now completed.");
      }
    );
  }
  /** Load more nodes from data source */
  loadMore(node: JcrFlatNode, onlyFirstTime = false) {
    const parent: JcrNode = this.jcrNodeMap[node.id];
    if (onlyFirstTime && parent.children!.length > 0) {
      this.resetCurrentOperations();
      return;
    }

    if (this.isRestNode(node)) {
      this.loadRestNode(parent, onlyFirstTime);
    }

    if (this.isWorkspace(node)) {
      this.loadWorkspace(parent, onlyFirstTime);
    }
  }

  loadChildren(node: JcrFlatNode, onlyFirstTime = true) {
    if (null != this.activeNode) {
      this.activeNode.active = false;
    }
    this.activeNode = node;

    node.active = true;
    this.loadMore(node, onlyFirstTime);
  }

  resetCurrentOperations() {
    if (this.activeNode && this.isRestNode(this.activeNode)) {
      let restNode = this.activeNode.value as RestNode;
      let jcrType = (this.activeNode.value as RestNode).jcrProperties.filter(
        (property) => property.name === "jcr:primaryType"
      )[0].values[0];
      this.currentNodeOperations =
        this.operationMap[jcrType] || this._resolveCurrentOperation(jcrType);
    } else {
      this.currentNodeOperations = null;
    }
  }
  private _resolveCurrentOperation(jcrType: string): WcmOperation[] {
    let key: string = Object.keys(this.operationMap).find((at) =>
      new RegExp(at).test(jcrType)
    );
    return key ? this.operationMap[key] : null;
  }
  doNodeOperation(item: String, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(
      this
    );
  }

  editCurrentNode(item) {
    console.log(`Click on Action 1 for ${item}`);
  }

  uploadZipFile() {
    //node: JcrFlatNode, matDialog: MatDialog) {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(UploadZipfileDialogComponent, {
      panelClass: "zipfile-upload-dialog",
      data: {
        wcmPath: (node.value as RestNode).wcmPath,
        repository: (node.value as RestNode).repository,
        workspace: (node.value as RestNode).workspace,
      },
    });
    dialogRef.afterClosed().subscribe((response) => {
      this.loadChildren(node, false);
      this.treeControl.expand(this.activeNode);
    });
  }

  createFolder() {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(NewFolderDialogComponent, {
      panelClass: "folder-new-dialog",
      data: {
        authoringTemplateForm: this.authoringTemplateFormMap[
          "bpwizard/default/system/folderType"
        ][0].formSchema,
        wcmPath: (node.value as RestNode).wcmPath,
        repository: (node.value as RestNode).repository,
        workspace: (node.value as RestNode).workspace,
      },
    });
    dialogRef.afterClosed().subscribe((response) => {
      this.loadChildren(node, false);
      this.treeControl.expand(this.activeNode);
    });
  }

  createTheme() {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(NewThemeDialogComponent, {
      panelClass: "theme-new-dialog",
      data: {
        authoringTemplateForm: this.authoringTemplateFormMap[
          WcmConstants.WCM_THEME_TYPE
        ][0].formSchema,
        wcmPath: (node.value as RestNode).wcmPath,
        repositoryName: (node.value as RestNode).repository,
        workspaceName: (node.value as RestNode).workspace,
      },
    });
    dialogRef.afterClosed().subscribe((response) => {
      this.loadChildren(node, false);
      this.treeControl.expand(this.activeNode);
    });
  }

  removeItem() {
    // node: JcrFlatNode, matDialog: MatDialog, modeshapeService: ModeshapeService,
    //   jcrNodeMap: Map<string, JcrNode>, nodeMap: Map<string, JcrFlatNode>, dataChange: BehaviorSubject<JcrNode[]>, callback: Function) {
    const node = this.activeNode;
    this.modeshapeService
      .deleteItem(
        (node.value as RestNode).repository,
        (node.value as RestNode).workspace,
        (node.value as RestNode).wcmPath
      )
      .subscribe((event: any) => {
        if (event.type === 4) {
          let jcrNode = this.jcrNodeMap[node.id];
          let parent = this.nodeMap[jcrNode.parent.id];
          jcrNode.parent.children.forEach((n) => {
            delete this.jcrNodeMap[n.id];
            delete this.nodeMap[n.id];
          });
          this.loadChildren(parent, false);
        }
      });
  }

  previewSiteArea() {
    const node = this.activeNode;
    this.router.navigate(["/wcm-authoring/preview"], {
      queryParams: {
        repository: (node.value as RestNode).repository,
        workspace: (node.value as RestNode).workspace,
        siteArea: (node.value as RestNode).wcmPath,
      },
    });
  }

  editSiteArea() {
    const node = this.activeNode;
    this.router.navigate(["/wcm-authoring/site-explorer/edit-sa"], {
      queryParams: {
        wcmPath: (node.value as RestNode).wcmPath,
        repository: (node.value as RestNode).repository,
        workspace: (node.value as RestNode).workspace,
        editing: true,
      },
    });
  }

  createSiteArea() {
    const node = this.activeNode;
    this.router.navigate(["/wcm-authoring/site-explorer/new-sa"], {
      queryParams: {
        wcmPath: (node.value as RestNode).wcmPath,
        repository: (node.value as RestNode).repository,
        workspace: (node.value as RestNode).workspace,
        editing: true,
      },
    });
  }

  createContent() {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(NewContentDialogComponent, {
      panelClass: "content-new-dialog",
      data: {
        authoringTemplateForm: this.authoringTemplateFormMap[
          "bpwizard/default/system/MyContent"
        ][0].formSchema,
        wcmPath: (node.value as RestNode).wcmPath,
        repository: (node.value as RestNode).repository,
        workspace: (node.value as RestNode).workspace,
      },
    });
    dialogRef.afterClosed().subscribe((response) => {
      this.loadChildren(node, false);
      this.treeControl.expand(this.activeNode);
    });
  }

  createSiteConfig() {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(NewSiteConfigDialogComponent, {
      panelClass: "siteconfig-new-dialog",
      data: {
        authoringTemplateForm: this.authoringTemplateFormMap[
          WcmConstants.WCM_SITE_CONFIG_TYPE
        ][0].formSchema,
        library: WcmUtils.library((node.value as RestNode).wcmPath),
        repository: (node.value as RestNode).repository,
        workspace: (node.value as RestNode).workspace,
      },
    });
    dialogRef.afterClosed().subscribe((response) => {
      this.loadChildren(node, false);
      this.treeControl.expand(this.activeNode);
    });
  }

  removeSiteConfig() {
    console.log("remove site config");
  }
}
