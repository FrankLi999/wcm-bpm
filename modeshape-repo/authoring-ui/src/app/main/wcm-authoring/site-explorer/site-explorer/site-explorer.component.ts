import { Component, Injectable, ViewChild } from '@angular/core';
import { FlatTreeControl } from '@angular/cdk/tree';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { BehaviorSubject, Observable } from 'rxjs';
import { MatMenuTrigger } from '@angular/material';
import { ModeshapeService } from '../../service/modeshape.service';

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

  constructor(private _database: SiteDatabase, private modeshapeService: ModeshapeService) {
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
    console.log(this.modeshapeService);
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