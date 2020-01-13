import { HasName } from './HasName';
import { ResourceNode } from './ResourceNode';
export interface Theme extends ResourceNode, HasName {
  library: string;
  repositoryName: string;
  workspace: string;
}