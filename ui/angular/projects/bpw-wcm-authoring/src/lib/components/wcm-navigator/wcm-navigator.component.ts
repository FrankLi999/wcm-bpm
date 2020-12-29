import { OnInit, OnDestroy, ViewChild, Input, Output, EventEmitter, Directive } from "@angular/core";
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
import cloneDeep from "lodash/cloneDeep";

import {
  WcmRepository,
  WcmOperation,
  WcmNode,
  WcmItemFilter,
  WcmService,
  OperationContext,
  WcmConstants,
} from "bpw-wcm-service";
import * as fromStore from "bpw-wcm-service";
import { SidebarService } from "bpw-common";
import {
  WcmItemTreeNodeData,
  WcmItemFlatTreeNode,
} from "../wcm-tree/model/wcm-tree.model";

@Directive()
export abstract class WcmNavigatorComponent implements OnInit, OnDestroy {
  activeNode: WcmItemFlatTreeNode = null;
  @ViewChild("contextMenu", { static: true })
  contextMenu: MatMenuTrigger;
  currentNodeOperations: WcmOperation[];
  // Flat tree data source
  dataSource: MatTreeFlatDataSource<WcmItemTreeNodeData, WcmItemFlatTreeNode>;
  dataChange = new BehaviorSubject<WcmItemTreeNodeData[]>([]);
  loadError: string;
  @Input() nodeFilter: WcmItemFilter;
  nodeMap: { [key: string]: WcmItemFlatTreeNode } = {};

  @Output() nodeOperationSelected: EventEmitter<
    OperationContext
  > = new EventEmitter<OperationContext>();
  @Output() nodeSelected: EventEmitter<WcmItemFlatTreeNode> = new EventEmitter<
    WcmItemFlatTreeNode
  >();
  @Input() operationMap: { [key: string]: WcmOperation[] };
  @Input() rootNode: string = "rootSiteArea";
  @Input() rootNodeType: string = "bpw:system_siteAreaType";
  searchInput: FormControl;
  @ViewChild("siteNavigator", { static: true }) tree;
  treeControl: FlatTreeControl<WcmItemFlatTreeNode>;
  treeFlattener: MatTreeFlattener<WcmItemTreeNodeData, WcmItemFlatTreeNode>;
  // functionMap: {[key:string]:Function}= {};
  // jcrNodeMap = new Map<string, JcrNode>();
  wcmNodeMap: { [key: string]: WcmItemTreeNodeData } = {};
  // jsonFormMap: {[key:string]:JsonForm} = {};
  workspace = WcmConstants.WS_DEFAULT;

  protected unsubscribeAll: Subject<any>;

  constructor(
    protected matDialog: MatDialog,
    protected sidebarService: SidebarService,
    protected store: Store<fromStore.WcmAppState>,
    protected wcmService: WcmService
  ) {
    this.unsubscribeAll = new Subject();
    // Set the defaults
    this.searchInput = new FormControl("");
  }

  ngOnInit() {
    this.nodeFilter = cloneDeep(this.nodeFilter);
    this.treeFlattener = new MatTreeFlattener(
      this.transformer,
      this.getLevel,
      this.isExpandable,
      this.getChildren
    );

    this.treeControl = new FlatTreeControl<WcmItemFlatTreeNode>(
      this.getLevel,
      this.isExpandable
    );

    this.dataSource = new MatTreeFlatDataSource(
      this.treeControl,
      this.treeFlattener
    );

    this.dataChange.subscribe((data) => {
      this.dataSource.data = data;
      this._resetCurrentNode();
      if (this.activeNode) {
        this.treeControl.expand(this.activeNode);
      }
    });
    this.store
      .pipe(
        select(fromStore.getGetWcmSystemError),
        takeUntil(this.unsubscribeAll)
      )
      .subscribe((loadError: string) => {
        if (loadError) {
          this.loadError = loadError;
        }
      });

    this.store
      .pipe(
        select(fromStore.getWcmRepositories),
        takeUntil(this.unsubscribeAll)
      )
      .subscribe((repositories: WcmRepository[]) => {
        if (repositories) {
          const libraryNodes: WcmItemTreeNodeData[] = [];
          repositories.forEach((repository) => {
            repository.workspaces.forEach((workspace) => {
              if (workspace.name === this.workspace) {
                workspace.libraries.forEach((library) => {
                  libraryNodes.push(
                    this._generateLibraryNode(
                      repository.name,
                      workspace.name,
                      library.name
                    )
                  );
                });
              }
            });
          });
          this.dataChange.next(libraryNodes);
          if (this.activeNode != null) {
            this.activeNode.active = true;
            this.treeControl.expand(this.activeNode);
          }
        }
      });
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.loadError && this.store.dispatch(new fromStore.WcmSystemClearError());
  }
  disableOperation(item: String, operation: WcmOperation) {
    return false;
  }

  doNodeOperation(item: String, operation: WcmOperation) {
    this.nodeOperationSelected.emit({
      wcmOperation: operation,
      node: this.activeNode.data,
    });
  }
  getChildren = (
    data: WcmItemTreeNodeData
  ): Observable<WcmItemTreeNodeData[]> => data.childrenChange;
  getLevel = (node: WcmItemFlatTreeNode) => node.level;
  hasChild = (_: number, _nodeData: WcmItemFlatTreeNode) => {
    return _nodeData.expandable;
  };
  isExpandable = (node: WcmItemFlatTreeNode) => {
    return node.expandable;
  };

  load(node: WcmItemFlatTreeNode) {
    this.loadChildren(node, false);
  }
  loadChildren(node: WcmItemFlatTreeNode, onlyFirstTime = true) {
    if (null != this.activeNode) {
      this.activeNode.active = false;
    }
    this.setActiveNode(node);
    node.active = true;
    this.loadMore(node, onlyFirstTime);
  }
  /** Load more nodes from data source */
  loadMore(node: WcmItemFlatTreeNode, onlyFirstTime = false) {
    const parent: WcmItemTreeNodeData = this.wcmNodeMap[node.data.id];
    if (onlyFirstTime && parent.children!.length > 0) {
      this._resetCurrentNode();
      return;
    }

    this.loadWcmNode(parent, onlyFirstTime);
  }
  loadWcmNode(parent: WcmItemTreeNodeData, onlyFirstTime = false) {
    this.nodeFilter.wcmPath = parent.wcmPath;
    this.wcmService
      .getWcmNodes(parent.repository, parent.workspace, this.nodeFilter)
      .subscribe((wcmNodes: WcmNode[]) => {
        if (wcmNodes) {
          const wcmTreeNodes: WcmItemTreeNodeData[] = wcmNodes.map((wcmNode) =>
            this._generateWcmNode(wcmNode)
          );
          parent.childrenChange.next(wcmTreeNodes);
          this.dataChange.next(this.dataChange.value);
          this._resetCurrentNode();
        }
      });
  }
  nodeRemoved(node: WcmItemFlatTreeNode) {
    let jcrNode = this.wcmNodeMap[node.data.id];
    let parent = this.nodeMap[jcrNode.parentId];
    let jcrNodeParent = this.wcmNodeMap[jcrNode.parentId];
    jcrNodeParent.children.forEach((n) => {
      delete this.wcmNodeMap[n.id];
      delete this.nodeMap[n.id];
    });
    this.loadChildren(parent, false);
  }
  onNodeSelected(node: WcmItemFlatTreeNode) {}
  parent(node: WcmItemFlatTreeNode): WcmItemFlatTreeNode {
    return node.data.parentId ? this.nodeMap[node.data.parentId] : null;
  }

  private _resetCurrentNode() {
    if (this.activeNode) {
      this.currentNodeOperations =
        this.operationMap[this.activeNode.data.nodeType] ||
        this._resolveCurrentOperation(this.activeNode.data.nodeType);
      let wcmNodes: WcmNode[] = [];
      if (
        this.activeNode.data.children.length &&
        this.activeNode.data.children.length > 0
      ) {
        this.activeNode.data.children.forEach((node) =>
          wcmNodes.push(node.data)
        );
      }
      this.store.dispatch(
        new fromStore.SetCurrentNode({
          currentNode: this.activeNode.data.data,
          children: wcmNodes,
        })
      );
    }
  }
  setActiveNode(jcrFlatNode: WcmItemFlatTreeNode) {
    this.activeNode = jcrFlatNode;
  }
  /**
   * Toggle the sidebar
   *
   * @param name
   */
  toggleSidebar(name): void {
    this.sidebarService.getSidebar(name).toggleOpen();
  }
  transformer = (data: WcmItemTreeNodeData, level: number) => {
    let jcrFlatNode = this.nodeMap[data.id];
    if (!jcrFlatNode) {
      jcrFlatNode = new WcmItemFlatTreeNode(
        data,
        level,
        data.children.length > 0,
        false
      );
      this.nodeMap[data.id] = jcrFlatNode;
    } else {
      jcrFlatNode.expandable = data.children.length > 0;
    }

    if (level == 0 && this.activeNode == undefined) {
      this.setActiveNode(jcrFlatNode);
    }
    return jcrFlatNode;
  };

  private _generateLibraryNode(
    repository: string,
    workspace: string,
    library: string
  ): WcmItemTreeNodeData {
    const libraryPath = `/${library}/${this.rootNode}`;
    let wcmNodeData: WcmItemTreeNodeData = this.wcmNodeMap[libraryPath];
    if (!wcmNodeData) {
      let wcmItemData: WcmNode = {
        name: library,
        repository: repository,
        workspace: workspace,
        nodeType: this.rootNodeType,
        wcmPath: libraryPath,
      };

      wcmNodeData = new WcmItemTreeNodeData(wcmItemData);
      this.wcmNodeMap[wcmNodeData.id] = wcmNodeData;
    }
    return wcmNodeData;
  }

  private _generateWcmNode(wcmNode: WcmNode): WcmItemTreeNodeData {
    let wcmNodeData: WcmItemTreeNodeData = this.wcmNodeMap[wcmNode.wcmPath];
    if (!wcmNodeData) {
      wcmNodeData = new WcmItemTreeNodeData({ ...wcmNode });
      this.wcmNodeMap[wcmNodeData.id] = wcmNodeData;
    }
    return wcmNodeData;
  }
  private _resolveCurrentOperation(jcrType: string): WcmOperation[] {
    let key: string = Object.keys(this.operationMap).find((at) =>
      new RegExp(at).test(jcrType)
    );
    return key ? this.operationMap[key] : null;
  }
}
