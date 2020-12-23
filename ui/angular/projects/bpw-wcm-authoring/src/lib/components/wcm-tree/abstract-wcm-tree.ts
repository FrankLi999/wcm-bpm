import { OnInit, OnDestroy, ViewChild, Input, Output, EventEmitter, Directive } from "@angular/core";

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

import {
  WcmItemTreeNodeData,
  WcmItemFlatTreeNode,
} from "./model/wcm-tree.model";

@Directive()
export abstract class AbstractWcmTree implements OnInit, OnDestroy {
  @Input() rootNode: string = "rootSiteArea";
  @Input() rootNodeType: string = "bpw:system_siteAreaType";
  @Input() nodeFilter: WcmItemFilter;
  @Input() operationMap: { [key: string]: WcmOperation[] };

  @Output() nodeSelected: EventEmitter<WcmNode> = new EventEmitter<WcmNode>();
  @Output() nodeOperationSelected: EventEmitter<
    OperationContext
  > = new EventEmitter<OperationContext>();
  // functionMap: {[key:string]:Function}= {};
  // jcrNodeMap = new Map<string, JcrNode>();
  wcmNodeMap: { [key: string]: WcmItemTreeNodeData } = {};
  // jsonFormMap: {[key:string]:JsonForm} = {};
  currentNodeOperations: WcmOperation[];
  activeNode: WcmItemFlatTreeNode = null;
  nodeMap: { [key: string]: WcmItemFlatTreeNode } = {};
  treeControl: FlatTreeControl<WcmItemFlatTreeNode>;
  treeFlattener: MatTreeFlattener<WcmItemTreeNodeData, WcmItemFlatTreeNode>;
  // Flat tree data source
  dataSource: MatTreeFlatDataSource<WcmItemTreeNodeData, WcmItemFlatTreeNode>;
  dataChange = new BehaviorSubject<WcmItemTreeNodeData[]>([]);
  @ViewChild("siteNavigator", { static: true }) tree;
  @ViewChild("contextMenu", { static: true })
  contextMenu: MatMenuTrigger;

  // Private
  protected unsubscribeAll: Subject<any>;
  loadError: string;

  constructor(
    protected wcmService: WcmService,
    protected store: Store<fromStore.WcmAppState>,
    protected matDialog: MatDialog
  ) {
    this.unsubscribeAll = new Subject();
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
      this.resetCurrentOperations();
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
              if (workspace.name === WcmConstants.WS_DEFAULT) {
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

  getChildren = (
    data: WcmItemTreeNodeData
  ): Observable<WcmItemTreeNodeData[]> => data.childrenChange;

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

  getLevel = (node: WcmItemFlatTreeNode) => node.level;

  isExpandable = (node: WcmItemFlatTreeNode) => {
    return node.expandable;
  };

  hasChild = (_: number, _nodeData: WcmItemFlatTreeNode) => {
    return _nodeData.expandable;
  };

  load(node: WcmItemFlatTreeNode) {
    this.loadChildren(node, false);
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
          this.resetCurrentOperations();
        }
      });
  }

  /** Load more nodes from data source */
  loadMore(node: WcmItemFlatTreeNode, onlyFirstTime = false) {
    const parent: WcmItemTreeNodeData = this.wcmNodeMap[node.data.id];
    if (onlyFirstTime && parent.children!.length > 0) {
      this.resetCurrentOperations();
      return;
    }

    this.loadWcmNode(parent, onlyFirstTime);
  }

  loadChildren(node: WcmItemFlatTreeNode, onlyFirstTime = true) {
    if (null != this.activeNode) {
      this.activeNode.active = false;
    }
    this.setActiveNode(node);
    node.active = true;
    this.loadMore(node, onlyFirstTime);
    return false;
  }

  resetCurrentOperations() {
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

  private _resolveCurrentOperation(jcrType: string): WcmOperation[] {
    let key: string = Object.keys(this.operationMap).find((at) =>
      new RegExp(at).test(jcrType)
    );
    return key ? this.operationMap[key] : null;
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

  onNodeSelected(node: WcmItemFlatTreeNode) {
    this.loadChildren(node);
    this.nodeSelected.emit(this.activeNode.data);
    return false;
  }

  parent(node: WcmItemFlatTreeNode): WcmItemFlatTreeNode {
    return node.data.parentId ? this.nodeMap[node.data.parentId] : null;
  }

  setActiveNode(jcrFlatNode: WcmItemFlatTreeNode) {
    this.activeNode = jcrFlatNode;
  }

  reloadActiveNode(loadParent) {
    if (this.activeNode) {
      this.load(
        loadParent(this.activeNode.data)
          ? this.parent(this.activeNode)
          : this.activeNode
      );
    }
  }
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
}
