import { HasName } from "./HasName";
import { BaseFormGroup } from "./BaseFormGroup";
import { FormControl } from "./FormControl";
import { ResourceNode } from "./ResourceNode";
import { AccessControlList } from "./AccessControlList";
import { AuthoringTemplateProperties } from "./AuthoringTemplateProperties";
import { WcmAuthority } from "./WcmAuthority";
export interface AuthoringTemplate extends ResourceNode, HasName {
  repository: string;
  workspace: string;
  library: string;
  baseType?: string;
  nodeType?: string;
  contentItemAcl?: AccessControlList;
  elementGroups?: BaseFormGroup[];
  elements?: { [key: string]: FormControl };
  properties?: AuthoringTemplateProperties;
  checkedOut?: boolean;
  wcmAuthority?: WcmAuthority;
}
