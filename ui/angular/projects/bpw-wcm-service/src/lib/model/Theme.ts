import { HasName } from "./HasName";
import { ResourceNode } from "./ResourceNode";
import { WcmAuthority } from "./WcmAuthority";
export interface Theme extends ResourceNode, HasName {
  library: string;
  repositoryName: string;
  workspace: string;
  wcmAuthority?: WcmAuthority;
}
