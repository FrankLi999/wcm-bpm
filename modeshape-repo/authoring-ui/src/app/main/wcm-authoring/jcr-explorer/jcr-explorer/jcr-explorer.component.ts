import { Component, OnInit, Injectable, ViewChild } from '@angular/core';
import { FlatTreeControl } from '@angular/cdk/tree';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { BehaviorSubject, Observable, forkJoin, of } from 'rxjs';
import { map, flatMap } from 'rxjs/operators';
import { MatMenuTrigger } from '@angular/material';
import { MatDialog } from '@angular/material/dialog';
import { ModeshapeService } from '../../service/modeshape.service';
import { 
  RestRepositories,
  RestWorkspaces,
  Workspace,
  Repository,
  RestNode
} from '../../model';
import { UploadZipfileDialogComponent } from '../../dialog/upload-zipfile-dialog/upload-zipfile-dialog.component';
import { NewFolderDialogComponent } from '../../dialog/new-folder-dialog/new-folder-dialog.component';
import { NewThemeDialogComponent } from '../../dialog/new-theme-dialog/new-theme-dialog.component';
/** Nested node */
export class JcrNode {
  childrenChange = new BehaviorSubject<JcrNode[]>([]);

  get children(): JcrNode[] {
    return this.childrenChange.value;
  }

  constructor(public id: string,
              public name: string,
              public value: Repository|Workspace|RestNode,
              public parent: JcrNode|null) {}
}

/** Flat node with expandable and level information */
export class JcrFlatNode {
  constructor(public id: string,
              public name: string,
              public value: Repository|Workspace|RestNode,
              public level = 1,
              public expandable = false,
              public active = false) {}
}

export class Operation {
  resourceName: String;
  operation: String;
  defaultTitle: String;
  defaultIcon: String;
}

@Component({
  selector: 'jcr-explorer',
  templateUrl: './jcr-explorer.component.html',
  styleUrls: ['./jcr-explorer.component.scss']
})
export class JcrExplorerComponent implements OnInit {
  functionMap = new Map<string, Function>();
  jcrNodeMap = new Map<string, JcrNode>();
  operationMap = new Map<string, Operation[]>();
  currentNodeOperations: Operation[];
  activeNode : JcrFlatNode = null; 
  nodeMap = new Map<string, JcrFlatNode>();
  treeControl: FlatTreeControl<JcrFlatNode>;
  treeFlattener: MatTreeFlattener<JcrNode, JcrFlatNode>;
  // Flat tree data source
  dataSource: MatTreeFlatDataSource<JcrNode, JcrFlatNode>;
  dataChange = new BehaviorSubject<JcrNode[]>([]);
  @ViewChild('jcrExplorer', {static: true}) tree;
  @ViewChild('contextMenu', {static: true}) contextMenu: MatMenuTrigger;

  constructor(
    private modeshapeService: ModeshapeService,
    private matDialog: MatDialog) {     
  }

  ngOnInit() {
    this.functionMap.set('Upload.File', this.uploadZipFile);
    this.functionMap.set('Create.Folder', this.createFolder);
    this.functionMap.set('Create.Theme', this.createTheme);
    this.functionMap.set('Remove.Folder', this.removeItem);
    this.functionMap.set('Remove.File', this.removeItem);
    this.functionMap.set('Delete.Theme', this.removeItem);
    
    this.treeFlattener = new MatTreeFlattener(this.transformer, this.getLevel,
      this.isExpandable, this.getChildren);

    this.treeControl = new FlatTreeControl<JcrFlatNode>(this.getLevel, this.isExpandable);

    this.dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);
  
    this.dataChange.subscribe(data => {
      this.dataSource.data = data;
      this.resetCurrentOperations();
    });
    this.modeshapeService.getRepositories().pipe(
      map((repositories: RestRepositories) => {
        return repositories ? repositories.repositories: [];
      }),
      flatMap((repositories: Repository[]) => {
        return forkJoin(
          repositories.map((repositoty: Repository) => {
            return this.modeshapeService.getWorkspaces(repositoty.name).pipe(
              map((workspaces: RestWorkspaces) => {
                repositoty.workspaces = workspaces ? workspaces.workspaces : [];
                return repositoty;
              })
            ); // end of pipe
          })
        ) // end of forkJoin
      }) //end of flatMap
    ).subscribe(
      (repositories: Repository[]) => {
        const repoNodes = repositories.map(repository => this.generateRepositoryNode(repository.name, repository));
        this.dataChange.next(repoNodes);
        if (this.activeNode != null) {
          this.activeNode.active = true;
          this.tree.treeControl.expand(this.activeNode);
        }
      },
      response => {
        console.log("Get Repository call ended in error", response);
        console.log(response);
      },
      () => {
        console.log("The Get Repository observable is now completed.");
      }
    );
  }

  private generateRepositoryNode(name: string, repository: Repository): JcrNode {
    let jcrNode: JcrNode = this.jcrNodeMap.get(`repo-${name}`);
    if (!jcrNode) {
      jcrNode = new JcrNode(`repo-${name}`, name, repository, null);
      const workspaceNodes = repository.workspaces.map((workspace: Workspace) => 
        this.generateWorkspaceNode(jcrNode, workspace.name, repository.name, workspace));
      
      this.jcrNodeMap.set(jcrNode.id, jcrNode);
      jcrNode.childrenChange.next(workspaceNodes);
    }
    return jcrNode;
  }

  private generateWorkspaceNode(repository: JcrNode, workspaceName: string, repositoryName: string, workspace: Workspace): JcrNode {
    if (this.jcrNodeMap.has(`ws-${workspaceName}`)) {
      return this.jcrNodeMap.get(workspaceName)!;
    }
    workspace.repositoryName = repositoryName;
    const workspaceNode = new JcrNode(`ws-${workspaceName}`, workspaceName, workspace, repository);
    this.jcrNodeMap.set(workspaceNode.id, workspaceNode);
    
    //load supported operations
    this.modeshapeService.getItems(repositoryName, workspaceName, 'bpwizard/configuration/operations', 2).subscribe(
      (restNode: RestNode) => {
        if (restNode) {
            restNode.children.forEach(child => {
                const jcrType = child.jcrProperties.filter(property => property.name==='bpw:jcrType')[0].values[0];
                let operations: Operation[] = [];
                child.children.forEach(operationNode => {
                  let operation: Operation = {
                    resourceName: '',
                    operation: '',
                    defaultTitle: '',
                    defaultIcon: 'dialpad'
                  };
                  operationNode.jcrProperties.forEach(property => {
                    if (property.name==='bpw:defaultTitle') {
                      operation.defaultTitle = property.values[0];
                    } else if (property.name==='bpw:operation') {
                      operation.operation = property.values[0];
                    } else if (property.name==='bpw:resourceName') {
                      operation.resourceName = property.values[0];
                    } else if (property.name==='bpw:defaultIcon') {
                      operation.defaultIcon = property.values[0];
                    }
                  });
                  operations.push(operation);
                });
                this.operationMap.set(jcrType, operations);
          });
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
    if (this.jcrNodeMap.has(`jcr-${jcrNodeName}`)) {
      return this.jcrNodeMap.get(jcrNodeName)!;
    }
    restNode.repositoryName = repositoryName;
    restNode.workspaceName = workspaceName;
    restNode.nodePath = nodePath? `${nodePath}/${jcrNodeName}` : jcrNodeName ;
    const jcrNodeNode = new JcrNode(`jcr-${restNode.nodePath}`, jcrNodeName, restNode, parent);
    this.jcrNodeMap.set(jcrNodeNode.id, jcrNodeNode);
    return jcrNodeNode;
  }

  getChildren = (node: JcrNode): Observable<JcrNode[]> => node.childrenChange;
  transformer = (node: JcrNode, level: number) => {
    let jcrFlatNode = this.nodeMap.get(node.id);
    if (!jcrFlatNode) {
      jcrFlatNode = new JcrFlatNode(node.id, node.name, node.value, level, node.children.length > 0, false);
      this.nodeMap.set(node.id, jcrFlatNode);
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
    const repositoryName = (parent.value as Workspace).repositoryName;
      const workspaceName = (parent.value as Workspace).name;
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
    const parent: JcrNode = this.jcrNodeMap.get(node.id);
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
      this.currentNodeOperations = this.operationMap.get(jcrType);
    } else {
      this.currentNodeOperations = null;
    }
  }
  doNodeOperation(item:String, operation:Operation) {

    // this.functionMap.get(`${operation.operation}.${operation.resourceName}`)(
    //   this.activeNode, 
    //   this.matDialog, 
    //   this.modeshapeService,
    //   this.jcrNodeMap,
    //   this.nodeMap,
    //   this.dataChange,
    //   this.loadChildren);
    this.functionMap.get(`${operation.operation}.${operation.resourceName}`).call(this);
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
    // node: JcrFlatNode, matDialog: MatDialog, modeshapeService: ModeshapeService, 
    //   jcrNodeMap: Map<string, JcrNode>, nodeMap: Map<string, JcrFlatNode>, dataChange: BehaviorSubject<JcrNode[]>, callback: Function) {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(NewFolderDialogComponent, {
      panelClass: 'folder-new-dialog',
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

  createTheme() {
    // node: JcrFlatNode, matDialog: MatDialog, modeshapeService: ModeshapeService, 
    //   jcrNodeMap: Map<string, JcrNode>, nodeMap: Map<string, JcrFlatNode>, dataChange: BehaviorSubject<JcrNode[]>, callback: Function) {
    const node = this.activeNode;
    let dialogRef = this.matDialog.open(NewThemeDialogComponent, {
      panelClass: 'theme-new-dialog',
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
          let jcrNode = this.jcrNodeMap.get(node.id);
          let parent = this.nodeMap.get(jcrNode.parent.id);
          jcrNode.parent.children.forEach(n => {
            this.jcrNodeMap.delete(n.id);
            this.nodeMap.delete(n.id);
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
}
