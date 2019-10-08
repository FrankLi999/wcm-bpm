import { OnInit, OnDestroy, ViewChild, Input, Output, EventEmitter } from '@angular/core';
import { FlatTreeControl } from '@angular/cdk/tree';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MatMenuTrigger } from '@angular/material';
import { MatDialog } from '@angular/material/dialog';
import { select, Store } from '@ngrx/store';
import cloneDeep from 'lodash-es/cloneDeep';
import { WcmService } from '../../service/wcm.service';
import { 
  WcmRepository,
  WcmOperation,
  WcmNode,
  WcmItemFilter
} from '../../model';

import * as fromStore from '../../store';

/** Nested node */
export class WcmItemTreeNode {
  childrenChange = new BehaviorSubject<WcmItemTreeNode[]>([]);

  get children(): WcmItemTreeNode[] {
    return this.childrenChange.value;
  }

  constructor(public id: string,
              public name: string,
              public repository: string,
              public workspace: string,             
              public nodeType: string,
              public wcmPath: string,
              public parent: WcmItemTreeNode|null) {}
}

/** Flat node with expandable and level information */
export class WcmItemFlatTreeNode {
  constructor(public id: string,
              public name: string, 
              public repository: string,
              public workspace: string,
              public nodeType: string,
              public wcmPath: string,
              public level = 1,
              public expandable = false,
              public active = false) {}
}

export class NodeOperation {
  wcmOperation: WcmOperation;
  node: WcmItemFlatTreeNode;
}
// @Component({
//   selector: 'site-navigator',
//   templateUrl: './site-navigator.component.html',
//   styleUrls: ['./site-navigator.component.scss']
// })
export abstract class WcmNavigatorComponent implements OnInit, OnDestroy {
  // @Input() uiService: SiteNavigatorUIService;
  @Input() rootNode: string = 'rootSiteArea';
  @Input() rootNodeType: string = 'bpw:siteArea';

  @Input() nodeFileter: WcmItemFilter;
  @Input() operationMap: {[key: string]: WcmOperation[]};

  @Output() nodeSelected: EventEmitter<WcmItemFlatTreeNode> = new EventEmitter();
  @Output() nodeOperation: EventEmitter<NodeOperation> = new EventEmitter();

  // functionMap: {[key:string]:Function}= {};
  // jcrNodeMap = new Map<string, JcrNode>();
  wcmNodeMap: {[key: string]: WcmItemTreeNode} = {};
  // jsonFormMap: {[key:string]:JsonForm} = {};
  currentNodeOperations: WcmOperation[];
  activeNode : WcmItemFlatTreeNode = null; 
  nodeMap : {[key: string]: WcmItemFlatTreeNode} = {};
  treeControl: FlatTreeControl<WcmItemFlatTreeNode>;
  treeFlattener: MatTreeFlattener<WcmItemTreeNode, WcmItemFlatTreeNode>;
  // Flat tree data source
  dataSource: MatTreeFlatDataSource<WcmItemTreeNode, WcmItemFlatTreeNode>;
  dataChange = new BehaviorSubject<WcmItemTreeNode[]>([]);
  @ViewChild('wcmNavigator', {static: true}) tree;
  @ViewChild('contextMenu', {static: true}) contextMenu: MatMenuTrigger;
  // Private
  protected unsubscribeAll: Subject<any>;
  loadError: string;
  constructor(
    protected wcmService: WcmService,
    protected store: Store<fromStore.WcmAppState>,
    protected matDialog: MatDialog) {

      this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.nodeFileter = cloneDeep(this.nodeFileter);
    // this.functionMap = this.uiService.getFunctionMap();

    this.treeFlattener = new MatTreeFlattener(this.transformer, this.getLevel,
      this.isExpandable, this.getChildren);

    this.treeControl = new FlatTreeControl<WcmItemFlatTreeNode>(this.getLevel, this.isExpandable);

    this.dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);
  
    this.dataChange.subscribe(data => {
      this.dataSource.data = data;
      this.resetCurrentOperations();
    });
    this.store.pipe(
      select(fromStore.getGetWcmSystemError),
      takeUntil(this.unsubscribeAll)       
    ).subscribe(
      (loadError: string) => {
      if (loadError) {
        this.loadError = loadError;
      }
    });

    this.store.pipe(
      select(fromStore.getWcmRepositories),
      takeUntil(this.unsubscribeAll)
    ).subscribe(
      (repositories: WcmRepository[]) => {
        if (repositories) {
          const libraryNodes: WcmItemTreeNode[] = [];
          repositories.forEach(repository => {
            repository.workspaces.forEach(workspace => {
              workspace.libraries.forEach(library => {
                libraryNodes.push(this.generateLibraryNode(repository.name, workspace.name, library.name));
              })
            })
          });
          this.dataChange.next(libraryNodes);
          if (this.activeNode != null) {
            this.activeNode.active = true;
            if (this.tree && this.tree.treeControl) {
              this.tree.treeControl.expand(this.activeNode);
            }
          }
        }
      },
      response => {
        console.log("Get Repository call ended in error", response);
        console.log(response);
      },
      () => {
        console.log("The Get Repository observable is now completed.");
      }
    )
  }

  /**
    * On destroy
    */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.loadError && this.store.dispatch(new fromStore.WcmSystemClearError());
  }

  private generateLibraryNode(repository: string, workspace:string, library: string): WcmItemTreeNode {
    const libraryPath = `${repository}/${workspace}/${library}`;
    let wcmNode: WcmItemTreeNode = this.wcmNodeMap[libraryPath];
    if (!wcmNode) {
      wcmNode = new WcmItemTreeNode(libraryPath, library, repository, workspace, this.rootNodeType, `bpwizard/library/${library}/${this.rootNode}`, null);
      this.wcmNodeMap[wcmNode.id] = wcmNode;
    }
    return wcmNode;
  }

  private generateWcmNode(parent:WcmItemTreeNode, wcmNodeName: string, repository: string, workspace: string, 
      nodeType: string, nodePath: string): WcmItemTreeNode {

    if (this.wcmNodeMap[nodePath]) {
      return this.wcmNodeMap[nodePath];
    }

    const wcmTreeNode = new WcmItemTreeNode(nodePath, wcmNodeName, repository, workspace, nodeType, nodePath, parent);
    this.wcmNodeMap[wcmTreeNode.id] = wcmTreeNode;
    return wcmTreeNode;
  }

  getChildren = (node: WcmItemTreeNode): Observable<WcmItemTreeNode[]> => node.childrenChange;
  transformer = (node: WcmItemTreeNode, level: number) => {
    let jcrFlatNode = this.nodeMap[node.id];
    if (!jcrFlatNode) {
      jcrFlatNode = new WcmItemFlatTreeNode(node.id, node.name, node.repository, node.workspace, node.nodeType, node.wcmPath, level, node.children.length > 0, false);
      this.nodeMap[node.id] = jcrFlatNode;
    } else {
      jcrFlatNode.expandable = node.children.length > 0;
    }
    
    if (level == 0 && this.activeNode == undefined) {
      this.activeNode = jcrFlatNode;
    }
    return jcrFlatNode;
  }

  getLevel = (node: WcmItemFlatTreeNode) => node.level;

  isExpandable = (node: WcmItemFlatTreeNode) => {
    return node.expandable;
  }

  hasChild = (_: number, _nodeData: WcmItemFlatTreeNode) => {
    return _nodeData.expandable;}

  load(node: WcmItemFlatTreeNode) {
    this.loadChildren(node, false);
    this.tree.treeControl.expand(node);
  }

  nodeRemoved(node:WcmItemFlatTreeNode) {
    let jcrNode = this.wcmNodeMap[node.id];
    let parent = this.nodeMap[jcrNode.parent.id];
    jcrNode.parent.children.forEach(n => {
      delete this.wcmNodeMap[n.id];
      delete this.nodeMap[n.id];
    });
    this.loadChildren(parent, false);
  }

  loadWcmNode(parent: WcmItemTreeNode, onlyFirstTime = false) {
    // const repositoryName = parent.repository;
    // const workspaceName = parent.workspace;
    // const wcmPath = parent.wcmPath;
    this.nodeFileter.nodePath = parent.wcmPath;
    this.wcmService.getWcmNodes(parent.repository, parent.workspace, this.nodeFileter).subscribe(
      (wcmNodes: WcmNode[]) => {
        if (wcmNodes) {
          const wcmTreeNodes: WcmItemTreeNode[] = wcmNodes.map(wcmNode => this.generateWcmNode(parent, wcmNode.name, parent.repository, parent.workspace, wcmNode.nodeType, wcmNode.wcmPath));
          parent.childrenChange.next(wcmTreeNodes);
          this.dataChange.next(this.dataChange.value);
        }
      },
      response => {
        console.log("GET call in error", response);
        console.log(response);
      },
      () => {
        console.log("The GET observable is now completed.");
      }
    );
  }

  
  /** Load more nodes from data source */
  loadMore(node:WcmItemFlatTreeNode, onlyFirstTime = false) {
    const parent: WcmItemTreeNode = this.wcmNodeMap[node.id];
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
    this.activeNode = node;

    node.active = true;
    this.loadMore(node, onlyFirstTime);
  }

  resetCurrentOperations() {      
    if (this.activeNode) {             
      this.currentNodeOperations = this.operationMap[this.activeNode.nodeType];
    } 
  }

  disableOperation(item: String, operation: WcmOperation) {
    return false;
  }

  doNodeOperation(item: String, operation: WcmOperation) {
    this.nodeOperation.emit({
      wcmOperation: operation,
      node: this.activeNode
    });
  }
}