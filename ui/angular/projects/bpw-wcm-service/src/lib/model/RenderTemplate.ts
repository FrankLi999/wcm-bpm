import { HasName } from "./HasName";
import { RenderTemplateLayoutRow } from "./RenderTemplateLayoutRow";
import { ResourceNode } from "./ResourceNode";
import { WcmAuthority } from "./WcmAuthority";
export interface RenderTemplate extends ResourceNode, HasName {
  repository: string;
  workspace: string;
  library: string;
  code?: string;
  preloop?: string;
  postloop?: string;
  maxEntries?: number;
  note?: string;
  query?: boolean;
  resourceName?: string;
  rows?: RenderTemplateLayoutRow[];
  wcmAuthority?: WcmAuthority;
}
