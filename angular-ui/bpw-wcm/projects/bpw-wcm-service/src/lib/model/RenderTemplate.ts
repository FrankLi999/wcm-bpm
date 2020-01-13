
import { HasName } from './HasName';
import { RenderTemplateLayoutRow } from './RenderTemplateLayoutRow';
import { ResourceNode } from './ResourceNode';
export interface RenderTemplate extends ResourceNode, HasName {
  repository: string;
  workspace: string;
  library: string;
  code?: string;
  preloop?: string;
  postloop?: string;
  maxEntries?: number;
  note?: string;
  isQuery?: boolean;
  resourceName?: string;
  rows?: RenderTemplateLayoutRow[];
}