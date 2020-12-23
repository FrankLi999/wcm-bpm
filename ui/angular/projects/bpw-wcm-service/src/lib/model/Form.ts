import { HasName } from "./HasName";
import { BaseFormGroup } from "./BaseFormGroup";
import { FormControl } from "./FormControl";
import { ResourceMixin } from "./ResourceMixin";
import { WcmAuthority } from "./WcmAuthority";
export interface Form extends ResourceMixin, HasName {
  repository: string;
  workspace: string;
  library: string;
  formLayout?: BaseFormGroup[];
  formControls?: { [key: string]: FormControl };
  wcmAuthority?: WcmAuthority;
}
