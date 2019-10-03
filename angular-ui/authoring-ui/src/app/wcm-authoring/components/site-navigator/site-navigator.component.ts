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
    SiteNavigatorFilter
  } from '../../model';
  
  import * as fromStore from '../../store';
  
  /** Nested node */
  export class WcmTreeNode {
    childrenChange = new BehaviorSubject<WcmTreeNode[]>([]);
  
    get children(): WcmTreeNode[] {
      return this.childrenChange.value;
    }
  
    constructor(public id: string,
                public name: string,
                public repository: string,
                public workspace: string,             
                public nodeType: string,
                public wcmPath: string,
                public parent: WcmTreeNode|null) {}
  }
  
  /** Flat node with expandable and level information */
export class WcmFlatTreeNode {
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
    node: WcmFlatTreeNode;
  }
  // @Component({
  //   selector: 'site-navigator',
  //   templateUrl: './site-navigator.component.html',
  //   styleUrls: ['./site-navigator.component.scss']
  // })
  export abstract class SiteNavigatorComponent implements OnInit, OnDestroy {
    // @Input() uiService: SiteNavigatorUIService;

    @Input() nodeFileter: SiteNavigatorFilter;
    @Input() operationMap: {[key: string]: WcmOperation[]};

    @Output() nodeSelected: EventEmitter<WcmFlatTreeNode> = new EventEmitter();
    @Output() nodeOperation: EventEmitter<NodeOperation> = new EventEmitter();
    // editCurrentNode(siteNavigator: SiteNavigatorComponent);

    // functionMap: {[key:string]:Function}= {};
    // jcrNodeMap = new Map<string, JcrNode>();
    wcmNodeMap: {[key: string]: WcmTreeNode} = {};
    // jsonFormMap: {[key:string]:JsonForm} = {};
    currentNodeOperations: WcmOperation[];
    activeNode : WcmFlatTreeNode = null; 
    nodeMap : {[key: string]: WcmFlatTreeNode} = {};
    treeControl: FlatTreeControl<WcmFlatTreeNode>;
    treeFlattener: MatTreeFlattener<WcmTreeNode, WcmFlatTreeNode>;
    // Flat tree data source
    dataSource: MatTreeFlatDataSource<WcmTreeNode, WcmFlatTreeNode>;
    dataChange = new BehaviorSubject<WcmTreeNode[]>([]);
    @ViewChild('siteNavigator', {static: true}) tree;
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
  
      this.treeControl = new FlatTreeControl<WcmFlatTreeNode>(this.getLevel, this.isExpandable);
  
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
            const libraryNodes: WcmTreeNode[] = [];
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
              if (this.tree.treeControl) {
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
  
    private generateLibraryNode(repository: string, workspace:string, library: string): WcmTreeNode {
      const libraryPath = `${repository}/${workspace}/${library}`;
      let wcmNode: WcmTreeNode = this.wcmNodeMap[libraryPath];
      if (!wcmNode) {
        wcmNode = new WcmTreeNode(libraryPath, library, repository, workspace, 'bpw:library', `bpwizard/library/${library}/rootSiteArea`, null);
        this.wcmNodeMap[wcmNode.id] = wcmNode;
      }
      return wcmNode;
    }
  
    private generateWcmNode(parent:WcmTreeNode, wcmNodeName: string, repository: string, workspace: string, 
        nodeType: string, nodePath: string): WcmTreeNode {

      if (this.wcmNodeMap[nodePath]) {
        return this.wcmNodeMap[nodePath];
      }

      const wcmTreeNode = new WcmTreeNode(nodePath, wcmNodeName, repository, workspace, nodeType, nodePath, parent);
      this.wcmNodeMap[wcmTreeNode.id] = wcmTreeNode;
      return wcmTreeNode;
    }
  
    getChildren = (node: WcmTreeNode): Observable<WcmTreeNode[]> => node.childrenChange;
    transformer = (node: WcmTreeNode, level: number) => {
      let jcrFlatNode = this.nodeMap[node.id];
      if (!jcrFlatNode) {
        jcrFlatNode = new WcmFlatTreeNode(node.id, node.name, node.repository, node.workspace, node.nodeType, node.wcmPath, level, node.children.length > 0, false);
        this.nodeMap[node.id] = jcrFlatNode;
      } else {
        jcrFlatNode.expandable = node.children.length > 0;
      }
      
      if (level == 0 && this.activeNode == undefined) {
        this.activeNode = jcrFlatNode;
      }
      return jcrFlatNode;
    }
  
    getLevel = (node: WcmFlatTreeNode) => node.level;
  
    isExpandable = (node: WcmFlatTreeNode) => {
      return node.expandable;
    }
  
    hasChild = (_: number, _nodeData: WcmFlatTreeNode) => {
      return _nodeData.expandable;}
  
    load(node: WcmFlatTreeNode) {
      this.loadChildren(node, false);
      this.tree.treeControl.expand(node);
    }

    nodeRemoved(node: WcmFlatTreeNode) {
      let jcrNode = this.wcmNodeMap[node.id];
      let parent = this.nodeMap[jcrNode.parent.id];
      jcrNode.parent.children.forEach(n => {
        delete this.wcmNodeMap[n.id];
        delete this.nodeMap[n.id];
      });
      this.loadChildren(parent, false);
    }

    loadWcmNode(parent: WcmTreeNode, onlyFirstTime = false) {
      // const repositoryName = parent.repository;
      // const workspaceName = parent.workspace;
      // const wcmPath = parent.wcmPath;
      this.nodeFileter.nodePath = parent.wcmPath;
      this.wcmService.getWcmNodes(parent.repository, parent.workspace, this.nodeFileter).subscribe(
        (wcmNodes: WcmNode[]) => {
          if (wcmNodes) {
            const wcmTreeNodes: WcmTreeNode[] = wcmNodes.map(wcmNode => this.generateWcmNode(parent, wcmNode.name, parent.repository, parent.workspace, wcmNode.nodeType, wcmNode.wcmPath));
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
    loadMore(node: WcmFlatTreeNode, onlyFirstTime = false) {
      const parent: WcmTreeNode = this.wcmNodeMap[node.id];
      if (onlyFirstTime && parent.children!.length > 0) {
        this.resetCurrentOperations();
        return;
      }
      
      this.loadWcmNode(parent, onlyFirstTime);
    }
  
    loadChildren(node: WcmFlatTreeNode, onlyFirstTime = true) {
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
  