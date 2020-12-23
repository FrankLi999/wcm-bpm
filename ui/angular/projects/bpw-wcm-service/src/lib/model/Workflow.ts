import { ResourceNode } from "./ResourceNode";
import { WcmAuthority } from "./WcmAuthority";
export interface Workflow extends ResourceNode {
  wcmAuthority?: WcmAuthority;
}
