import { ResourceNode } from "./ResourceNode";
import { HasName } from "./HasName";
import { WcmAuthority } from "./WcmAuthority";
export interface BpmnWorkflow extends ResourceNode, HasName {
  repository?: string;
  workspace?: string;
  library?: string;
  bpmn?: string;
  wcmAuthority?: WcmAuthority;
}
