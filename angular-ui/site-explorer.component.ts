import { Component, Injectable, ViewChild } from '@angular/core';
import { FlatTreeControl } from '@angular/cdk/tree';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { BehaviorSubject, Observable } from 'rxjs';
import { MatMenuTrigger } from '@angular/material';
// import { ModeshapeService } from '../../service/modeshape.service';

const LOAD_MORE = 'LOAD_MORE';

/** Nested node */
export class SiteNode {
  childrenChange = new BehaviorSubject<SiteNode[]>([]);

  get children(): SiteNode[] {
    return this.childrenChange.value;
  }

  constructor(public item: string,
              public hasChildren = false,
              public loadMoreParentItem: string | null = null) {}
}

/** Flat node with expandable and level information */
export class SiteFlatNode {
  constructor(public item: string,
              public level = 1,
              public expandable = false,
              public active = false,
              public loadMoreParentItem: string | null = null) {}
}

/**
 * A database that only load part of the data initially. After user clicks on the `Load more`
 * button, more data will be loaded.
 */
@Injectable()
export class SiteDatabase {
  batchNumber = 5;
  dataChange = new BehaviorSubject<SiteNode[]>([]);
  nodeMap = new Map<string, SiteNode>();

  /** The data */
  rootLevelNodes: string[] = ["My Site"];
  dataMap = new Map<string, string[]>([
    ["My Site", ['Fruits', 'Vegetables']],
    ['Fruits', ['Apple', 'Orange', 'Banana']],
    ['Vegetables', ['Tomato', 'Potato', 'Onion']],
    ['Apple', ['Fuji', 'Macintosh']],
    ['Onion', ['Yellow', 'White', 'Purple', 'Green', 'Shallot', 'Sweet', 'Red', 'Leek']],
  ]);

  initialize() {
    const data = this.rootLevelNodes.map(name => this._generateNode(name));
    this.dataChange.next(data);
  }

  /** Expand a node whose children are not loaded */
  loadMore(item: string, onlyFirstTime = false) {
    if (!this.nodeMap.has(item) || !this.dataMap.has(item)) {
      return;
    }
    const parent = this.nodeMap.get(item)!;
    const children = this.dataMap.get(item)!;
    if (onlyFirstTime && parent.children!.length > 0) {
      return;
    }
    const newChildrenNumber = parent.children!.length + this.batchNumber;
    const nodes = children.slice(0, newChildrenNumber)
      .map(name => this._generateNode(name));
    if (newChildrenNumber < children.length) {
      // Need a new load more node
      nodes.push(new SiteNode(LOAD_MORE, false, item));
    }
    parent.childrenChange.next(nodes);
    this.dataChange.next(this.dataChange.value);
  }

  private _generateNode(item: string): SiteNode {
    if (this.nodeMap.has(item)) {
      return this.nodeMap.get(item)!;
    }
    const result = new SiteNode(item, this.dataMap.has(item));
    this.nodeMap.set(item, result);
    return result;
  }
}

@Component({
  selector: 'site-explorer',
  templateUrl: './site-explorer.component.html',
  styleUrls: ['./site-explorer.component.scss'],
  providers: [SiteDatabase]
})
export class SiteExplorerComponent {
  activeNode : SiteFlatNode = null; 
  nodeMap = new Map<string, SiteFlatNode>();
  treeControl: FlatTreeControl<SiteFlatNode>;
  treeFlattener: MatTreeFlattener<SiteNode, SiteFlatNode>;
  // Flat tree data source
  dataSource: MatTreeFlatDataSource<SiteNode, SiteFlatNode>;
  @ViewChild('site', {static: true}) tree;
  @ViewChild('contextMenu', {static: true}) contextMenu: MatMenuTrigger;

  constructor(
    //private modeshapeService: ModeshapeService,
    private _database: SiteDatabase
  ) {
    this.treeFlattener = new MatTreeFlattener(this.transformer, this.getLevel,
      this.isExpandable, this.getChildren);

    this.treeControl = new FlatTreeControl<SiteFlatNode>(this.getLevel, this.isExpandable);

    this.dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);

    _database.dataChange.subscribe(data => {
      this.dataSource.data = data;
    });
    _database.initialize();
    if (this.activeNode != null) {
      this.activeNode.active = true;
      this._database.loadMore(this.activeNode.item, true);
    }
  }

  ngAfterViewInit() {
    //console.log(this.modeshapeService);
    this.tree.treeControl.expand(this.activeNode);
  }

  getChildren = (node: SiteNode): Observable<SiteNode[]> => node.childrenChange;

  transformer = (node: SiteNode, level: number) => {
    const existingNode = this.nodeMap.get(node.item);
    if (existingNode) {
      return existingNode;
    }
    const newNode = new SiteFlatNode(node.item, level, node.hasChildren, false, node.loadMoreParentItem);
    this.nodeMap.set(node.item, newNode);
    if (level == 0) {
      this.activeNode = newNode;
    }
    return newNode;
  }

  getLevel = (node: SiteFlatNode) => node.level;

  isExpandable = (node: SiteFlatNode) => node.expandable;

  hasChild = (_: number, _nodeData: SiteFlatNode) => _nodeData.expandable;

  isLoadMore = (_: number, _nodeData: SiteFlatNode) => _nodeData.item === LOAD_MORE;

  /** Load more nodes from data source */
  loadMore(item: string) {
    this._database.loadMore(item);
  }

  loadChildren(node: SiteFlatNode) {
    if (null != this.activeNode) {
      this.activeNode.active = false;
     
    }
    this.activeNode = node;
    node.active = true;
    this._database.loadMore(node.item, true);
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

  newSiteAreaNode(item: String) {
    alert(`Click on Action 2 for ${item}`);
  }

  newContentItem(item: String) {
    alert(`Click on Action 2 for ${item}`);
  }

  newFile(item: String) {
    alert(`Click on Action 2 for ${item}`);
  }
}

.center {
    padding: 2.6em;
}
#template {
    display: flex;
    flex-direction: row;
    align-self: flex-start;
}

<div id="template" class="page-layout carded left-sidebar inner-scroll">
    <!-- SIDEBAR -->
    <fuse-sidebar fxFlex="20" class="sidebar" name="page-template-side-bar" position="left" lockedOpen="gt-md">
        <div fxLayout="column" fxLayoutAlign="start"  fxFlex="1 0 auto">
          <site-navigator [siteNavigatorUIService]="siteExplorerUIService">
          </site-navigator>
          <!--
          <mat-tree #site [dataSource]="dataSource" [treeControl]="treeControl">
              <mat-tree-node *matTreeNodeDef="let node" matTreeNodePadding>
                <button mat-icon-button 
                    [attr.aria-label]="'toggle ' + node.filename"
                    (click)="loadChildren(node)"
                    matTreeNodeToggle>
                  <mat-icon class="mat-icon-rtl-mirror">chevron_right</mat-icon>
                  <mat-icon class="mat-icon-rtl-mirror">
                      {{node.active ? 'folder_open' : 'folder'}}
                  </mat-icon>                
                </button>
                <span style="margin-left: 8px;" [matMenuTriggerFor]="contextMenu"
		                  [matMenuTriggerData]="{item: node.item}">
                    {{node.item}}
                </span>
              </mat-tree-node>

              <mat-tree-node *matTreeNodeDef="let node; when: hasChild" matTreeNodePadding>
                <button mat-icon-button
                    [attr.aria-label]="'toggle ' + node.filename"
                    (click)="loadChildren(node)"                    
                    matTreeNodeToggle>
                  <mat-icon class="mat-icon-rtl-mirror">
                    {{treeControl.isExpanded(node) ? 'expand_more' : 'chevron_right'}}
                  </mat-icon>
                  <mat-icon class="mat-icon-rtl-mirror">
                      {{node.active ? 'folder_open' : 'folder'}}
                  </mat-icon>
                </button>
                <span style="margin-left: 8px;" [matMenuTriggerFor]="contextMenu"
                        [matMenuTriggerData]="{item: node.item}">
                    {{node.item}}
                </span>
              </mat-tree-node>

              <mat-tree-node *matTreeNodeDef="let node; when: isLoadMore">
                <button mat-button (click)="loadMore(node.loadMoreParentItem)">
                  Load more...
                </button>
              </mat-tree-node>
            </mat-tree>
            -->  
        </div>
    </fuse-sidebar>
    <!-- / SIDEBAR -->
    <!-- CENTER -->
    <div class="center" fxFlex="80">
        <!-- FOLDER OVERVIEW -->       
        <h1>Active node: {{activeNode.item}}</h1>
        <folder-overview></folder-overview>
        <!-- / FOLDER OVERVIEW -->
    </div>
    <!-- / CENTER -->
</div>
<mat-menu #contextMenu="matMenu">
  <ng-template matMenuContent let-item="item">
    <button mat-menu-item (click)="editCurrentNode(item)">Edit</button>
    <button mat-menu-item (click)="deleteCurrentNode(item)">Delete</button>
    <button mat-menu-item (click)="newSiteAreaNode(item)">New Site Area</button>
    <button mat-menu-item (click)="newPageNode(item)">New Page</button>
    <button mat-menu-item (click)="newContentItem(item)">New Content</button>
    <button mat-menu-item (click)="newFile(item)">New File</button>
  </ng-template>
</mat-menu>
