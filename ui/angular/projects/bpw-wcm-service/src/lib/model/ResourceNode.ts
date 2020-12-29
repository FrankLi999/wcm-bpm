import { AccessControlEntry } from "./AccessControlEntry";
import { ResourceMixin } from "./ResourceMixin";

export interface ResourceNode extends ResourceMixin {
  lockOwner?: string;
  acl?: AccessControlEntry;
}
