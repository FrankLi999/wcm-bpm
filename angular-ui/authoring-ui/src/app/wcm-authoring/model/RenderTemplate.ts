
import { HasName } from './HasName';

export interface RenderTemplate extends HasName {
  repository: string;
  workspace: string;
  library: string;
  title?: string;
  description?: string;
  code: string;
  preloop?: string;
  postloop?: string;
  maxEntries?: number;
  note: string;
  isQuery: boolean;
  resourceName?: string;
}