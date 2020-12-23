import { ContentItemProperties } from "./ContentItemProperties";
import { KeyValues } from "./KeyValues";
import { SearchData } from "./SearchData";
import { AccessControlEntry } from "./AccessControlEntry";
import { WorkflowNode } from "./WorkflowNode";
export interface ContentItem {
  id: string;
  name?: string;
  locked?: boolean;
  checkedOut?: boolean;
  repository: string;
  workspace: string;
  wcmPath: string;
  workflow?: WorkflowNode;
  authoringTemplate?: string;
  nodeType?: string;
  acl?: AccessControlEntry;
  metadata?: KeyValues;
  searchData?: SearchData;

  elements?: { [key: string]: string };
  properties?: ContentItemProperties;
}
