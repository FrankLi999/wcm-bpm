import { WcmOperation, WcmNode, WcmUtils } from "bpw-wcm-service";
import { BehaviorSubject } from "rxjs";
export class WcmItemTreeNodeData {
  childrenChange = new BehaviorSubject<WcmItemTreeNodeData[]>([]);
  get children(): WcmItemTreeNodeData[] {
    return this.childrenChange.value;
  }
  get id(): string {
    return this._itemData.wcmPath;
  }
  get name(): string {
    return this._itemData.name;
  }
  get repository(): string {
    return this._itemData.repository;
  }
  get workspace(): string {
    return this._itemData.workspace;
  }
  get nodeType(): string {
    return this._itemData.nodeType;
  }
  get wcmPath(): string {
    return this._itemData.wcmPath;
  }
  get parentId(): string {
    return WcmUtils.parentPath(this._itemData.wcmPath);
  }
  get data(): WcmNode {
    return this._itemData;
  }
  constructor(private _itemData: WcmNode) {}
}

/** Flat node with expandable and level information */
export class WcmItemFlatTreeNode {
  constructor(
    public data: WcmItemTreeNodeData,
    public level = 1,
    public expandable = false,
    public active = false
  ) {}
}
