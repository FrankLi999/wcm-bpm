import { Component, OnInit, Injectable, ViewChild } from '@angular/core';
import { FlatTreeControl } from '@angular/cdk/tree';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { BehaviorSubject, Observable, forkJoin, of } from 'rxjs';
import { map, flatMap } from 'rxjs/operators';
import { MatMenuTrigger } from '@angular/material';
import { ModeshapeService } from '../../service/modeshape.service';
import { 
  RestRepositories,
  RestWorkspaces,
  Workspace,
  Repository,
  RestNode,
  HasName
} from '../../model';
const LOAD_MORE = 'LOAD_MORE';

/** Nested node */
export class JcrNode {
  childrenChange = new BehaviorSubject<JcrNode[]>([]);

  get children(): JcrNode[] {
    return this.childrenChange.value;
  }

  constructor(public id: string,
              public name: string,
              public value: Repository|Workspace|RestNode,
              public hasChildren = false,
              public loadMoreParentItem: string | null = null) {}
}

/** Flat node with expandable and level information */
export class JcrFlatNode {
  constructor(public id: string,
              public name: string,
              public value: Repository|Workspace|RestNode,
              public level = 1,
              public expandable = false,
              public active = false,
              public loadMoreParentItem: string | null = null) {}
}

@Component({
  selector: 'jcr-explorer',
  templateUrl: './jcr-explorer.component.html',
  styleUrls: ['./jcr-explorer.component.scss']
})
export class JcrExplorerComponent implements OnInit {
  jcrNodeMap = new Map<string, JcrNode>();
  activeNode : JcrFlatNode = null; 
  nodeMap = new Map<string, JcrFlatNode>();
  treeControl: FlatTreeControl<JcrFlatNode>;
  treeFlattener: MatTreeFlattener<JcrNode, JcrFlatNode>;
  // Flat tree data source
  dataSource: MatTreeFlatDataSource<JcrNode, JcrFlatNode>;
  dataChange = new BehaviorSubject<JcrNode[]>([]);
  @ViewChild('site', {static: true}) tree;
  @ViewChild('contextMenu', {static: true}) contextMenu: MatMenuTrigger;

  constructor(private modeshapeService: ModeshapeService) {     
  }

  ngOnInit() {
    this.treeFlattener = new MatTreeFlattener(this.transformer, this.getLevel,
      this.isExpandable, this.getChildren);

    this.treeControl = new FlatTreeControl<JcrFlatNode>(this.getLevel, this.isExpandable);

    this.dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);
  
    this.dataChange.subscribe(data => {
      this.dataSource.data = data;
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
      const workspaceNodes = repository.workspaces.map((workspace: Workspace) => 
        this.generateWorkspaceNode(workspace.name, repository.name, workspace));
      jcrNode = new JcrNode(`repo-${name}`, name, repository, workspaceNodes.length > 0);
      this.jcrNodeMap.set(jcrNode.id, jcrNode);
      jcrNode.childrenChange.next(workspaceNodes);
    }
    return jcrNode;
  }

  private generateWorkspaceNode(workspaceName: string, repositoryName: string, workspace: Workspace): JcrNode {
    if (this.jcrNodeMap.has(`ws-${workspaceName}`)) {
      return this.jcrNodeMap.get(workspaceName)!;
    }
    workspace.repositoryName = repositoryName;
    const workspaceNode = new JcrNode(`ws-${workspaceName}`, workspaceName, workspace, true);//this.jcrNodeMap.has(workspaceName));
    this.jcrNodeMap.set(workspaceNode.id, workspaceNode);
    return workspaceNode;
  }

  private generateJcrNodeNode(jcrNodeName: string, workspaceName: string, repositoryName: string, nodePath: string, restNode: RestNode): JcrNode {
    if (this.jcrNodeMap.has(`jcr-${jcrNodeName}`)) {
      return this.jcrNodeMap.get(jcrNodeName)!;
    }
    restNode.repositoryName = repositoryName;
    restNode.workspaceName = workspaceName;
    restNode.nodePath = nodePath? `${nodePath}/${jcrNodeName}` : jcrNodeName ;
    const jcrNodeNode = new JcrNode(`jcr-${restNode.nodePath}`, jcrNodeName, restNode, true);//this.jcrNodeMap.has(jcrNodeName));
    this.jcrNodeMap.set(jcrNodeNode.id, jcrNodeNode);
    return jcrNodeNode;
  }

  getChildren = (node: JcrNode): Observable<JcrNode[]> => node.childrenChange;
  transformer = (node: JcrNode, level: number) => {
    let jcrFlatNode = this.nodeMap.get(node.id);
    if (!jcrFlatNode) {
      jcrFlatNode = new JcrFlatNode(node.id, node.name, node.value, level, node.hasChildren, false, node.loadMoreParentItem);
      this.nodeMap.set(node.id, jcrFlatNode);
    }
    if (level == 0 && this.activeNode == undefined) {
      this.activeNode = jcrFlatNode;
    }
    return jcrFlatNode;
  }

  getLevel = (node: JcrFlatNode) => node.level;

  isExpandable = (node: JcrFlatNode) => node.expandable;

  hasChild = (_: number, _nodeData: JcrFlatNode) => _nodeData.expandable;

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
    this.modeshapeService.getItems(repositoryName, workspaceName, nodePath).subscribe(
      (restNode: RestNode) => {
        if (restNode && restNode.children) {
          (parent.value as RestNode).customProperties = restNode.customProperties; 
          (parent.value as RestNode).jcrProperties = restNode.jcrProperties;
          const restNodes = restNode.children.map(jcrNode => this.generateJcrNodeNode(jcrNode.name, workspaceName, repositoryName, nodePath, jcrNode));
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
            const restNodes = restNode.children.map(restNode => this.generateJcrNodeNode(restNode.name, workspaceName, repositoryName, nodePath, restNode));
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
      return;
    }
    
    if (this.isRestNode(node)) {
      this.loadRestNode(parent, onlyFirstTime);
    }

    if (this.isWorkspace(node)) {
      this.loadWorkspace(parent, onlyFirstTime);
    }
  }

  loadChildren(node: JcrFlatNode) {
    if (null != this.activeNode) {
      this.activeNode.active = false;
    }
    this.activeNode = node;
    node.active = true;
    this.loadMore(node, true);
  }

  editCurrentNode(item: String) {
    alert(`Click on Action 1 for ${item}`);
  }

  deleteCurrentNode(item: String) {
    alert(`Click on Action 2 for ${item}`);
  }

  newPageNode(item: String) {
    alert(`Click on Action 2 for ${item}`);
  }

  newFolderNode(item: String) {
    alert(`Click on Action 2 for ${item}`);
  }

  newContentItem(item: String) {
    alert(`Click on Action 2 for ${item}`);
  }

  newFile(item: String) {
    alert(`Click on Action 2 for ${item}`);
  }
}
