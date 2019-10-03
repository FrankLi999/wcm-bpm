import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { FlatTreeControl } from '@angular/cdk/tree';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MatMenuTrigger } from '@angular/material';
import { MatDialog } from '@angular/material/dialog';
import { select, Store } from '@ngrx/store';
import { ModeshapeService } from '../../service/modeshape.service';

import { 
  WcmRepository,
  WcmWorkspace,
  RestNode,
  //ApplicationConfig,
  WcmOperation,
  // Workspace,
  JsonForm
} from '../../model';

import { UploadZipfileDialogComponent } from '../../dialog/upload-zipfile-dialog/upload-zipfile-dialog.component';
import { NewFolderDialogComponent } from '../../dialog/new-folder-dialog/new-folder-dialog.component';
import { NewThemeDialogComponent } from '../../dialog/new-theme-dialog/new-theme-dialog.component';
import { NewSiteareaDialogComponent } from '../../dialog/new-sitearea-dialog/new-sitearea-dialog.component';
import { NewSiteConfigDialogComponent } from '../../dialog/new-site-config-dialog/new-site-config-dialog.component';
import { NewContentDialogComponent } from '../../dialog/new-content-dialog/new-content-dialog.component';
import * as fromStore from '../../store';

/** Nested node */
class JcrNode {
  childrenChange = new BehaviorSubject<JcrNode[]>([]);

  get children(): JcrNode[] {
    return this.childrenChange.value;
  }

  constructor(public id: string,
              public name: string,
              public value: WcmRepository|WcmWorkspace|RestNode,
              public parent: JcrNode|null) {}
}

/** Flat node with expandable and level information */
class JcrFlatNode {
  constructor(public id: string,
              public name: string,
              public value: WcmRepository|WcmWorkspace|RestNode,
              public level = 1,
              public expandable = false,
              public active = false) {}
}

@Component({
  selector: 'jcr-explorer',
  templateUrl: './jcr-explorer.component.html',
  styleUrls: ['./jcr-explorer.component.scss']
})
export class JcrExplorerComponent implements OnInit, OnDestroy {
  functionMap: {[key:string]:Function}= {};
  // jcrNodeMap = new Map<string, JcrNode>();
  jcrNodeMap: {[key: string]: JcrNode} = {};
  operationMap: {[key: string]: WcmOperation[]} = {};
  jsonFormMap: {[key:string]:JsonForm} = {};
  currentNodeOperations: WcmOperation[];
  activeNode : JcrFlatNode = null; 
  nodeMap : {[key: string]: JcrFlatNode} = {};
  treeControl: FlatTreeControl<JcrFlatNode>;
  treeFlattener: MatTreeFlattener<JcrNode, JcrFlatNode>;
  // Flat tree data source
  dataSource: MatTreeFlatDataSource<JcrNode, JcrFlatNode>;
  dataChange = new BehaviorSubject<JcrNode[]>([]);
  loadError: string;
  @ViewChild('jcrExplorer', {static: true}) tree;
  @ViewChild('contextMenu', {static: true}) contextMenu: MatMenuTrigger;
  // Private
  private unsubscribeAll: Subject<any>;
  
  constructor(
    private modeshapeService: ModeshapeService,
    // private wcmService: WcmService,
    private store: Store<fromStore.WcmAppState>,
    private matDialog: MatDialog) {
      this.unsubscribeAll = new Subject();
  }

  ngOnInit() {
    this.functionMap['Upload.file'] = this.uploadZipFile;
    this.functionMap['Create.folder'] = this.createFolder;
    this.functionMap['Create.theme'] = this.createTheme;
    this.functionMap['Remove.folder'] = this.removeItem;
    this.functionMap['Remove.file'] = this.removeItem;
    this.functionMap['Delete.theme'] = this.removeItem;
    
    this.functionMap['Create.siteArea'] = this.createSiteArea;
    this.functionMap['Delete.siteArea'] = this.removeItem;
    this.functionMap['Create.content'] = this.createContent;
    this.functionMap['Delete.content'] = this.removeItem;
    this.functionMap['Create.siteConfig'] = this.createSiteConfig;
    this.functionMap['Delete.siteConfig'] = this.removeSiteConfig;

    this.treeFlattener = new MatTreeFlattener(this.transformer, this.getLevel,
      this.isExpandable, this.getChildren);

    this.treeControl = new FlatTreeControl<JcrFlatNode>(this.getLevel, this.isExpandable);

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
          const repoNodes = repositories.map(repository => this.generateRepositoryNode(repository.name, repository));
          this.dataChange.next(repoNodes);
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
    
    this.store.pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getJsonForms)
    ).subscribe(
      (jsonForms: {[key:string]:JsonForm}) => {
        if (jsonForms) {
          this.jsonFormMap = jsonForms;
        }
      },
      response => {
        console.log("getAuthoringTemplateAsJsonSchema call ended in error", response);
        console.log(response);
      },
      () => {
        console.log("getAuthoringTemplateAsJsonSchema observable is now completed.");
      }
    );
  }

  /**
    * On destroy
    */
  ngOnDestroy(): void {
    this.unsubscribeAll.next();
    this.unsubscribeAll.complete();
    this.loadError && this.store.dispatch(new fromStore.WcmSystemClearError());
  }

  ngAfterViewInit() {
    //console.log(this.modeshapeService);
    this.tree.treeControl.expand(this.activeNode);
  }
  
  private generateRepositoryNode(name: string, repository: WcmRepository): JcrNode {
    const repoId = `repo-${name}`;
    let jcrNode: JcrNode = this.jcrNodeMap[repoId];
    if (!jcrNode) {
      jcrNode = new JcrNode(repoId, name, repository, null);
      const workspaceNodes = repository.workspaces.map((workspace: WcmWorkspace) => 
        this.generateWorkspaceNode(jcrNode, workspace.name, repository.name, workspace));
      
      this.jcrNodeMap[jcrNode.id] = jcrNode;
      jcrNode.childrenChange.next(workspaceNodes);
    }
    return jcrNode;
  }

  private generateWorkspaceNode(repository: JcrNode, workspaceName: string, repositoryName: string, workspace: WcmWorkspace): JcrNode {
    const wsId = `ws-${workspaceName}`;
    if (this.jcrNodeMap[wsId]) {
      return this.jcrNodeMap[wsId];
    }

    // workspace.repositoryName = repositoryName;
    const workspaceNode = new JcrNode(wsId, workspaceName, workspace, repository);
    this.jcrNodeMap[workspaceNode.id] =  workspaceNode;
    
    //load supported operations
    //this.modeshapeService.getItems(repositoryName, workspaceName, 'bpwizard/library/system/configuration/operations', 2).subscribe(
      this.store.pipe(
        takeUntil(this.unsubscribeAll),
        select(fromStore.getOperations)).subscribe(
      (operations: {[key: string]: WcmOperation[]}) => {
        if (operations) {
          this.operationMap = operations;
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
    return workspaceNode;
  }

  private generateJcrNodeNode(parent:JcrNode, jcrNodeName: string, workspaceName: string, repositoryName: string, nodePath: string, restNode: RestNode): JcrNode {
    nodePath = nodePath? `${nodePath}/${jcrNodeName}` : jcrNodeName ;
    const nodeId = `jcr-${nodePath}`;
    if (this.jcrNodeMap[nodeId]) {
      return this.jcrNodeMap[nodeId];
    }
    restNode.repositoryName = repositoryName;
    restNode.workspaceName = workspaceName;
    restNode.nodePath = nodePath;
    const jcrNodeNode = new JcrNode(nodeId, jcrNodeName, restNode, parent);
    this.jcrNodeMap[jcrNodeNode.id] = jcrNodeNode;
    return jcrNodeNode;
  }

  getChildren = (node: JcrNode): Observable<JcrNode[]> => node.childrenChange;
  transformer = (node: JcrNode, level: number) => {
    let jcrFlatNode = this.nodeMap[node.id];
    if (!jcrFlatNode) {
      jcrFlatNode = new JcrFlatNode(node.id, node.name, node.value, level, node.children.length > 0, false);
      this.nodeMap[node.id] = jcrFlatNode;
    } else {
      jcrFlatNode.expandable = node.children.length > 0;
    }
    
    if (level == 0 && this.activeNode == undefined) {
      this.activeNode = jcrFlatNode;
    }
    return jcrFlatNode;
  }

  getLevel = (node: JcrFlatNode) => node.level;

  isExpandable = (node: JcrFlatNode) => {
    return node.expandable;
  }

  hasChild = (_: number, _nodeData: JcrFlatNode) => {
    return _nodeData.expandable;}

  // isLoadMore = (_: number, _nodeData: JcrFlatNode) => _nodeData.name === LOAD_MORE;
  isRestNode(node: JcrFlatNode): boolean {
      return node ? node.id.startsWith('jcr-') : false; 
  }

  isRepository(node: JcrFlatNode): boolean {
    return node ? node.id.startsWith('repo-') : false;
  }

  isWorkspace(node: JcrFlatNode): boolean {
    return node ? node.id.startsWith('ws-') : false;
  }

  loadRestNode(parent: JcrNode, onlyFirstTime = false) {
    const repositoryName = (parent.value as RestNode).repositoryName;
    const workspaceName = (parent.value as RestNode).workspaceName;
    const nodePath = (parent.value as RestNode).nodePath;
    this.modeshapeService.getItems(repositoryName, workspaceName, nodePath, 2).subscribe(
      (restNode: RestNode) => {
        if (restNode && restNode.children) {
          (parent.value as RestNode).customProperties = restNode.customProperties; 
          (parent.value as RestNode).jcrProperties = restNode.jcrProperties;
          const restNodes = restNode.children.map(jcrNode => this.generateJcrNodeNode(parent, jcrNode.name, workspaceName, repositoryName, nodePath, jcrNode));
          parent.childrenChange.next(restNodes);
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

  loadWorkspace(parent: JcrNode, onlyFirstTime = false) {
    const repositoryName = (parent.parent.value as WcmRepository).name;
      const workspaceName = (parent.value as WcmWorkspace).name;
      const nodePath = "";
      this.modeshapeService.getItems(repositoryName, workspaceName, nodePath).subscribe(
        (restNode: RestNode) => {
          if (restNode && restNode.children) {
            const restNodes = restNode.children.map(restNode => this.generateJcrNodeNode(parent, restNode.name, workspaceName, repositoryName, nodePath, restNode));
            parent.childrenChange.next(restNodes);
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
      let jcrType = (this.activeNode.value as RestNode).jcrProperties.filter(property => property.name ==='jcr:primaryType')[0].values[0];
      this.currentNodeOperations = this.operationMap[jcrType];
    } else {
      this.currentNodeOperations = null;
    }
  }
  
  doNodeOperation(item:String, operation: WcmOperation) {
    this.functionMap[`${operation.operation}.${operation.resourceName}`].call(this);
  }

  editCurrentNode(item) {
    console.log(`Click on Action 1 for ${item}`);
  }

  uploadZipFile() { //node: JcrFlatNode, matDialog: MatDialog) {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(UploadZipfileDialogComponent, {
      panelClass: 'zipfile-upload-dialog',
      data: { 
        nodePath: (node.value as RestNode).nodePath,
        repositoryName: (node.value as RestNode).repositoryName,
        workspaceName: (node.value as RestNode).workspaceName
      }
    });
    dialogRef.afterClosed()
      .subscribe(response => {

        this.loadChildren(node, false);
        this.tree.treeControl.expand(this.activeNode);
          
    });
  }

  createFolder() {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(NewFolderDialogComponent, {
      panelClass: 'folder-new-dialog',
      data: { 
        jsonFormObject: this.jsonFormMap['bpwizard/default/system/folderType'].formSchema,
        nodePath: (node.value as RestNode).nodePath,
        repositoryName: (node.value as RestNode).repositoryName,
        workspaceName: (node.value as RestNode).workspaceName
      }
    });
    dialogRef.afterClosed()
      .subscribe(response => {
        this.loadChildren(node, false);
        this.tree.treeControl.expand(this.activeNode);
          
    });
  }

  createTheme() {
    // node: JcrFlatNode, matDialog: MatDialog, modeshapeService: ModeshapeService, 
    //   jcrNodeMap: Map<string, JcrNode>, nodeMap: Map<string, JcrFlatNode>, dataChange: BehaviorSubject<JcrNode[]>, callback: Function) {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(NewThemeDialogComponent, {
      panelClass: 'theme-new-dialog',
      data: { 
        jsonFormObject: this.jsonFormMap['bpwizard/default/system/themeType'].formSchema,
        nodePath: (node.value as RestNode).nodePath,
        repositoryName: (node.value as RestNode).repositoryName,
        workspaceName: (node.value as RestNode).workspaceName
      }
    });
    dialogRef.afterClosed()
      .subscribe(response => {
        this.loadChildren(node, false);
        this.tree.treeControl.expand(this.activeNode);
          
    });
  }

  removeItem() {
    // node: JcrFlatNode, matDialog: MatDialog, modeshapeService: ModeshapeService, 
    //   jcrNodeMap: Map<string, JcrNode>, nodeMap: Map<string, JcrFlatNode>, dataChange: BehaviorSubject<JcrNode[]>, callback: Function) {
    const node = this.activeNode;
    this.modeshapeService.deleteItem(
      (node.value as RestNode).repositoryName,
      (node.value as RestNode).workspaceName,
      (node.value as RestNode).nodePath
    ).subscribe(
      (event: any) => {
        if (event.type===4) {
          let jcrNode = this.jcrNodeMap[node.id];
          let parent = this.nodeMap[jcrNode.parent.id];
          jcrNode.parent.children.forEach(n => {
            delete this.jcrNodeMap[n.id];
            delete this.nodeMap[n.id];
          });
          this.loadChildren(parent, false);
        }
      },
      response => {
        console.log("removeFolder call in error", response);
        console.log(response);
      },
      () => {
        console.log("removeFolder observable is now completed.");
      });
  }


  createSiteArea() {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(NewSiteareaDialogComponent, {
      panelClass: 'sitearea-new-dialog',
      data: { 
        jsonFormObject: this.jsonFormMap['bpwizard/default/system/siteAreaType'].formSchema,
        nodePath: (node.value as RestNode).nodePath,
        repositoryName: (node.value as RestNode).repositoryName,
        workspaceName: (node.value as RestNode).workspaceName
      }
    });
    dialogRef.afterClosed()
      .subscribe(response => {
        this.loadChildren(node, false);
        this.tree.treeControl.expand(this.activeNode);
          
    });
  }
  
  createContent() {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(NewContentDialogComponent, {
      panelClass: 'content-new-dialog',
      data: { 
        jsonFormObject: this.jsonFormMap['bpwizard/default/system/MyContent'].formSchema,
        nodePath: (node.value as RestNode).nodePath,
        repositoryName: (node.value as RestNode).repositoryName,
        workspaceName: (node.value as RestNode).workspaceName
      }
    });
    dialogRef.afterClosed()
      .subscribe(response => {
        this.loadChildren(node, false);
        this.tree.treeControl.expand(this.activeNode);
          
    });
  }

  createSiteConfig() {
    const node = this.activeNode;
    console.log(node);
    let dialogRef = this.matDialog.open(NewSiteConfigDialogComponent, {
      panelClass: 'siteconfig-new-dialog',
      data: { 
        jsonFormObject: this.jsonFormMap['bpwizard/default/system/siteConfigType'].formSchema,
        // nodePath: (node.value as RestNode).nodePath,
        library: 'camunda',
        repositoryName: (node.value as RestNode).repositoryName,
        workspaceName: (node.value as RestNode).workspaceName
      }
    });
    dialogRef.afterClosed()
      .subscribe(response => {
        this.loadChildren(node, false);
        this.tree.treeControl.expand(this.activeNode);
          
    });
  }

  removeSiteConfig() {
    console.log('remove site config');
  }
}
